package com.yourLuck.yourLuck.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomJoinResponse {
    private Integer roomId;
    private String chatRoomName;
    private String userName;

}

