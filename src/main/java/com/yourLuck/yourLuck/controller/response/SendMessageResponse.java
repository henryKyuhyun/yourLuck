package com.yourLuck.yourLuck.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SendMessageResponse {

    private Integer messageId;
    private String userName;

    private String messageContent;
    private LocalDateTime timestamp;
}
