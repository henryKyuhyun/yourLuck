package com.yourLuck.yourLuck.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import java.util.ArrayList;
import java.util.List;

public class RedisMessageSubscriber implements MessageListener {

    public static List<String> messageList = new ArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        messageList.add(message.toString());
//        TODO : 추가적 메시지 처리 로직
    }
}
