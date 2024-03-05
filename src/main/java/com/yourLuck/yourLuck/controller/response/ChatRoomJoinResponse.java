package com.yourLuck.yourLuck.controller.response;

import com.yourLuck.yourLuck.model.entity.ChatRoomEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomJoinResponse {
    private Integer roomId;
    private String chatRoomName;
    private String userName;

}

