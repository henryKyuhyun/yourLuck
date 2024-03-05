package com.yourLuck.yourLuck.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MessageReceiveResponse {
    private Integer messageId;
    private Integer userId;
    private String userName;
    private String messageContent;
    private LocalDateTime timestamp;
}
