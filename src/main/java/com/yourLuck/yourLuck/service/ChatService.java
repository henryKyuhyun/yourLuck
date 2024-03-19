package com.yourLuck.yourLuck.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import com.yourLuck.yourLuck.repository.MessageCacheRepository;
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
    private final MessageCacheRepository messageCacheRepository;

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
    public SendMessageResponse sendMessage(Integer chatRoomId, String messageContent, SendMessageRequest sendMessageRequest) {

        ChatRoomEntity chatRoomEntity = getChatRoomEntityOrException(chatRoomId);
        UserEntity userEntity = userEntityRepository.findById(sendMessageRequest.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String userName = userEntity.getUserName();
//        MessageEntity messageEntity = MessageEntity.of(chatRoomEntity,sendMessageRequest.getMessageContent(), userName);
        MessageEntity messageEntity = MessageEntity.of(chatRoomEntity, userEntity, messageContent, userName);

        messageRepository.save(messageEntity);

        // 메시지 전송
        Integer messageId = messageEntity.getId();
        String destination = String.format("/topic/chatroom.%d", chatRoomId);
        messagingTemplate.convertAndSend(destination, Message.fromEntity(messageEntity));
        LocalDateTime timestamp = messageEntity.getRegisteredAt().toLocalDateTime();
        SendMessageResponse response = new SendMessageResponse(messageId, userName, sendMessageRequest.getMessageContent(), timestamp);

        // Redis 캐싱 처리를 MessageCacheRepository를 통해 수행
        messageCacheRepository.saveMessage(chatRoomId, Message.fromEntity(messageEntity));

        return response;
    }


private Message convertToDto(MessageEntity messageEntity) {
    return new Message(messageEntity.getId(), messageEntity.getChatRoomEntity().getId(),  messageEntity.getUserName(), messageEntity.getMessage(), messageEntity.getRegisteredAt());
}
    public List<Message> getMessagesFromChatRoom(Integer chatRoomId) {
        // Redis에서 메시지 불러오기를 MessageCacheRepository를 통해 수행
        List<Message> messages = messageCacheRepository.getMessages(chatRoomId);

        if (messages.isEmpty()) {
            // Redis에 메시지가 없는 경우, DB에서 불러와 Redis에 저장
            List<MessageEntity> messageEntities = messageRepository.findByChatRoomEntity_Id(chatRoomId);
            messages = messageEntities.stream().map(this::convertToDto).collect(Collectors.toList());

            // 불러온 메시지들을 Redis에 저장
            messages.forEach(message -> messageCacheRepository.saveMessage(chatRoomId, message));
        }

        return messages;
    }




}


