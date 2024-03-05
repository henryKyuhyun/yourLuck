package com.yourLuck.yourLuck.controller.response;

import com.yourLuck.yourLuck.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class CreateChatRoomResponse {
    private Integer id;
    private String chatRoomName;

}
