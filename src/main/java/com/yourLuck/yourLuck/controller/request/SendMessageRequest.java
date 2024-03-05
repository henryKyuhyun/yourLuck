package com.yourLuck.yourLuck.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendMessageRequest {

    private Integer userId;
    private String messageContent;
}
