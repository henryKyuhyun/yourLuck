package com.yourLuck.yourLuck.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /*
        클라이언트가 /pub/hello 로 메시지를 받는다
        /sub/channel/channelID - 구독
        /pub/hello -메시지 발생
    */
}
