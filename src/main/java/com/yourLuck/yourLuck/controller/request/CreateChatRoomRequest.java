package com.yourLuck.yourLuck.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateChatRoomRequest {

    private String chatRoomName;
    private String userName;
}
