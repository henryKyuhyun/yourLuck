package com.yourLuck.yourLuck.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table
@Getter @Setter
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_name")
    private String chatRoomName;

//@JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // 한 유저가 여러 채팅방에 참여하고, 여러 유저가 한 채팅방에 참여할 수 있는 ManyToMany
    @ManyToMany
    @JoinTable(
            name = "chatroom_user",  // 조인 테이블의 이름
            joinColumns = @JoinColumn(name = "chatroom_id"),  // 현재 엔티티를 참조하는 외래 키
            inverseJoinColumns = @JoinColumn(name = "user_id")  // 반대쪽 엔티티를 참조하는 외래 키
    )
    private Set<UserEntity> users = new HashSet<>();


    @OneToMany(mappedBy = "chatRoomEntity", cascade = CascadeType.ALL)
    private Set<MessageEntity> messageEntities = new HashSet<>();


    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @PrePersist
    void registeredAt(){
        this.registeredAt = Timestamp.from(Instant.now());
    }

    public static ChatRoomEntity of(String chatRoomName, UserEntity userEntity){
        ChatRoomEntity entity = new ChatRoomEntity();
        entity.setChatRoomName(chatRoomName);
        entity.setUser(userEntity);
        return entity;
    }
}
