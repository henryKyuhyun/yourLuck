package com.yourLuck.yourLuck.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourLuck.yourLuck.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MessageCacheRepository {

    private final static Duration MESSAGE_CACHE_TTL = Duration.ofDays(1);
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate messageRedisTemplate;

    public void saveMessage(Integer chatRoomId, Message message){
        String key = getKey(chatRoomId);
        log.info("SET Messages to Redis {} : {}", key, message);
        try{
            String messageJson = objectMapper.writeValueAsString(message);
            messageRedisTemplate.opsForList().rightPush(key, messageJson);
            messageRedisTemplate.expire(key,MESSAGE_CACHE_TTL);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Message> getMessages(Integer chatRoomId) {
        String key = getKey(chatRoomId);
        List<String> messagesList = messageRedisTemplate.opsForList().range(key, 0, -1);
        if (messagesList != null && !messagesList.isEmpty()) {
            return messagesList.stream().map(obj -> {
                try {
                    return objectMapper.readValue(obj.toString(), Message.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    private String getKey(Integer chatRoomId) {
        return "CHATROOM_MESSAGES" + chatRoomId;
    }
}
