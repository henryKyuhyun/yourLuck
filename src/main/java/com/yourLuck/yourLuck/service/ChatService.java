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
import com.yourLuck.yourLuck.repository.MessageRepository;
import com.yourLuck.yourLuck.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final RedisTemplate<String, Object> redisTemplate;

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
        String userName = userEntity.getUserName();
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

        String redisKey = "chatRoom:" + chatRoomId;

        //Redis에  메시징 캐싱
        Message messageDto = Message.fromEntity(messageEntity);
        redisTemplate.opsForList().rightPush(redisKey, messageDto);
        return response;
    }


//    public List<Message> getMessagesFromChatRoom(Integer chatRoomId){
//        // Redis에서 채팅방 메시지 조회 시도
//        String redisKey = "chatRoom:" + chatRoomId;
//        // Redis 에서 메시지 리스트 조회
//        List<Object> rawMessageEntities = redisTemplate.opsForList().range(redisKey, 0, -1);// 모든 메시지 조회
//        if (rawMessageEntities == null || rawMessageEntities.isEmpty()) {
//            // Redis에 데이터가 없는 경우 DB에서 조회 후 Redis에 저장
//            List<MessageEntity> messageEntities = messageRepository.findByChatRoomEntity_Id(chatRoomId);
//            rawMessageEntities = new ArrayList<>(messageEntities);
//            // Redis에 저장
//            messageEntities.forEach(messageEntity -> redisTemplate.opsForList().rightPush(redisKey, messageEntity));
//        }
//        return rawMessageEntities.stream()
//                .filter(object -> object instanceof MessageEntity)
//                .map(object -> (MessageEntity) object)
//                .map(Message::fromEntity)
//                .collect(Collectors.toList());
//
//    }
// MessageEntity를 MessageDto로 변환하는 메서드 예제
private Message convertToDto(MessageEntity messageEntity) {
    return new Message(messageEntity.getId(), messageEntity.getChatRoomEntity().getId(), messageEntity.getUserName(), messageEntity.getMessage(), messageEntity.getRegisteredAt());
}
    public List<Message> getMessagesFromChatRoom(Integer chatRoomId) {
        String redisKey = "chatRoom:" + chatRoomId;
        List<Object> objects = redisTemplate.opsForList().range(redisKey, 0, -1);
        List<Message> messages = new ArrayList<>();

        if (objects != null && !objects.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // LocalDateTime 처리를 위해
            // JSON 문자열을 Message 객체로 변환합니다.
            messages = objects.stream()
                    .map(obj -> {
                        try {
                            return objectMapper.readValue(obj.toString(), Message.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
        } else {
            // Redis에 데이터가 없는 경우 DB에서 조회
            List<MessageEntity> messageEntities = messageRepository.findByChatRoomEntity_Id(chatRoomId);
            // DB에서 조회한 Entity를 DTO로 변환
            messages = messageEntities.stream().map(this::convertToDto).collect(Collectors.toList());

            ObjectMapper objectMapper = new ObjectMapper();
            // Redis에 DTO 리스트를 저장하기 전에 JSON 문자열로 변환
            try {
                String messagesJson = objectMapper.writeValueAsString(messages);
                redisTemplate.opsForList().rightPush(redisKey, messagesJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return messages;
    }



}


