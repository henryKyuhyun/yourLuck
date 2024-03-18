package com.yourLuck.yourLuck.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.yourLuck.yourLuck.controller.response.CreateChatRoomResponse;
import com.yourLuck.yourLuck.model.entity.ChatRoomEntity;
import com.yourLuck.yourLuck.model.entity.MessageEntity;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ChatRoom {

    private Integer id;
    private String chatRoomName;
    private User user;
    private Set<User> users;
    private Set<Message> messageEntities;
    private Timestamp registeredAt;

    public static ChatRoom fromEntity(ChatRoomEntity chatRoomEntity) {
        return new ChatRoom(
                chatRoomEntity.getId(),
                chatRoomEntity.getChatRoomName(),
                User.fromEntity(chatRoomEntity.getUser()),
                chatRoomEntity.getUsers().stream().map(User::fromEntity).collect(Collectors.toSet()),
                chatRoomEntity.getMessageEntities().stream().map(Message::fromEntity).collect(Collectors.toSet()),
                chatRoomEntity.getRegisteredAt()
        );
    }

    public CreateChatRoomResponse createChatRoomResponse() {
        return new CreateChatRoomResponse(this.id, this.chatRoomName);
    }
}
