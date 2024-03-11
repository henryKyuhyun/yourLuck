package com.yourLuck.yourLuck.service;

import com.yourLuck.yourLuck.controller.request.SendMessageRequest;
import com.yourLuck.yourLuck.controller.response.SendMessageResponse;
import com.yourLuck.yourLuck.exception.ErrorCode;
import com.yourLuck.yourLuck.exception.LuckApplicationException;
import com.yourLuck.yourLuck.model.ChatRoom;
import com.yourLuck.yourLuck.model.Message;
import com.yourLuck.yourLuck.model.entity.ChatRoomEntity;
import com.yourLuck.yourLuck.model.entity.MessageEntity;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import com.yourLuck.yourLuck.repository.ChatRoomRepository;
import com.yourLuck.yourLuck.repository.MessageRepository;
import com.yourLuck.yourLuck.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {


    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserEntityRepository userEntityRepository;
    private final SimpMessagingTemplate messagingTemplate; // 메시지 전송을 위한 SimpMessagingTemplate 주입

    private UserEntity getUserEntityOrException(String userName) {
        return userEntityRepository.findByUserName(userName).orElseThrow(()->
                new LuckApplicationException(ErrorCode.USER_NOT_FOUNDED, String.format("%s not founded", userName)));
    }


    private ChatRoomEntity getChatRoomEntityOrException(Integer chatRoomId){
        return chatRoomRepository.findById(chatRoomId).orElseThrow(()->
                new LuckApplicationException(ErrorCode.USER_NOT_FOUNDED, String.format("%s not founded", chatRoomId)));
    }
    @Transactional
    public ChatRoom createChatRoom(String chatRoomName, String userName) {
        // 채팅방 이름으로 기존 채팅방이 있는지 체크
        Optional<ChatRoomEntity> existingChatRoom = chatRoomRepository.findByChatRoomName(chatRoomName);
        if(existingChatRoom.isPresent()) {
        // 이미 채팅방이 존재하므로 에러를 던지기
        throw new LuckApplicationException(ErrorCode.ALREADY_EXISTING_CHAT_ROOM_NAME, "The ChatRoomName already existing");
    }
    // 새로운 채팅방 생성 로직
        UserEntity userEntity = getUserEntityOrException(userName);
        ChatRoomEntity chatRoomEntity = chatRoomRepository.save(ChatRoomEntity.of(chatRoomName, userEntity));
        return ChatRoom.fromEntity(chatRoomEntity);
    }


    @Transactional
    public ChatRoom joinChatRoom(Integer chatRoomId, String userName, String chatRoomName) {
        UserEntity userEntity = getUserEntityOrException(userName);
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findByIdOrChatRoomName(chatRoomId, chatRoomName)
                .orElseThrow(() -> new LuckApplicationException(ErrorCode.CHATROOM_NOT_FOUND, "ChatRoom not found with given ID or Name"));

        chatRoomEntity.getUsers().add(userEntity);
        chatRoomRepository.save(chatRoomEntity);
        return ChatRoom.fromEntity(chatRoomEntity);
    }

    @Transactional
    public SendMessageResponse sendMessage(Integer chatRoomId, String messageContent,SendMessageRequest sendMessageRequest) {
        // chatRoom과 userEntity를 찾음
        ChatRoomEntity chatRoomEntity = getChatRoomEntityOrException(chatRoomId);
        UserEntity userEntity = userEntityRepository.findById(sendMessageRequest.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // userName을 추출
        String userName = userEntity.getUserName();
        // messageEntity를 생성하고 저장, 이 때부터 id 사용 가능
        MessageEntity messageEntity = MessageEntity.of(chatRoomEntity, sendMessageRequest.getMessageContent(), userName);
        messageRepository.save(messageEntity);

        // 저장된 messageEntity에서 id 추출
        Integer messageId = messageEntity.getId();

        // 메시지 전송을 위한 토픽
        String destination = String.format("/topic/chatroom.%d", chatRoomId);
        messagingTemplate.convertAndSend(destination, Message.fromEntity(messageEntity));

        // messageEntity의 Timestamp 값을 LocalDateTime으로 변환
        LocalDateTime timestamp = messageEntity.getRegisteredAt().toLocalDateTime();

        // SendMessageResponse 객체를 생성하여 반환
        SendMessageResponse response = new SendMessageResponse(messageId, userName, sendMessageRequest.getMessageContent(), timestamp);

        return response;
    }


    public List<Message> getMessagesFromChatRoom(Integer chatRoomId){
        List<MessageEntity> messageEntities = messageRepository.findByChatRoomEntity_Id(chatRoomId);
        return messageEntities.stream()
                .map(Message::fromEntity)
                .collect(Collectors.toList());
    }
    //주어진 채팅방 ID에 해당하는 모든 메시지의 목록을 조회하는 기존의 데이터베이스 쿼리를 이용한 방식입니다. 이 메소드는 특정 채팅방의 이전 메시지들을 가져올 때 유용하며, 예를 들어 사용자가 채팅방에 처음 진입할 때 지난 대화 내용을 로드하는 데 사용될 수 있습니다.


//    TODO :실시간으로 메시지를 송수신하기 위해서는 클라이언트 측에서 서버로 메시지 발송 요청을 /app/sendMessage와 같은 엔드포인트를 통해 STOMP 프로토콜을 사용하여 전달하고, 서버는 SimpMessagingTemplate을 사용해 /topic/chatroom.{chatRoomId} 주소를 구독하는 모든 클라이언트에게 메시지를 전달해야 합니다. 따라서, 실시간으로 채팅 메시지를 수신하는 것은 getMessagesFromChatRoom 메소드가 아닌 클라이언트 측에서 설정된 STOMP 구독을 통해 이루어집니다. 예를 들어 클라이언트 측 JavaScript에서 SockJS와 STOMP 클라이언트를 사용하여 실시간 메시지를 수신할 수 있습니다.
//    예시 : // SockJS와 STOMP 클라이언트 초기화
//const socket = new SockJS('/ws-stomp');
//const stompClient = Stomp.over(socket);
//
//// 서버에 연결하고 특정 채팅방을 구독
//stompClient.connect({}, function (frame) {
//    const chatRoomId = 1; // 예시 채팅방 ID
//    stompClient.subscribe(`/topic/chatroom.${chatRoomId}`, function (messageOutput) {
//        // 여기서 받은 메시지를 처리 (예: 화면에 표시)
//        const message = JSON.parse(messageOutput.body);
//        console.log(message);
//    });
//});


}
