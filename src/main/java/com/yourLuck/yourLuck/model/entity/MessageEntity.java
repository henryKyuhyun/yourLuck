package com.yourLuck.yourLuck.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table
@Getter@Setter
public class MessageEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoomEntity chatRoomEntity;

    @ManyToOne
    private UserEntity user;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "message")
    private String message;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @PrePersist
    void registeredAt(){
        this.registeredAt = Timestamp.from(Instant.now());
    }
    public static MessageEntity of(ChatRoomEntity chatRoomEntity, UserEntity user, String message, String userName) {
        MessageEntity entity = new MessageEntity();
        entity.setChatRoomEntity(chatRoomEntity);
        entity.setUser(user);
        entity.setMessage(message);
        entity.setUserName(userName);
        return entity;
    }

}
