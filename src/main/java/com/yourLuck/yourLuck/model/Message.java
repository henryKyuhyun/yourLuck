package com.yourLuck.yourLuck.model;

import com.yourLuck.yourLuck.model.entity.MessageEntity;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Message {

    private Integer id;
    private Integer chatRoomId;
    private String userName;
    private String message;
    private Timestamp registeredAt;

    public static Message fromEntity(MessageEntity messageEntity) {
        UserEntity userEntity = messageEntity.getUser();
        String userName;
        // UserEntity가 null인지 체크하여 안전하게 userName을 설정합니다.
        if (userEntity != null) {
            userName = userEntity.getUserName();
        } else {
            // 적절한 기본값이나 예외 처리를 수행합니다.
            userName = "Unknown"; // 예시로 "Unknown"이라는 기본값을 사용합니다.
        }

        // ChatRoom 객체 생성 대신에 chatRoom ID만 저장합니다.
        Integer chatRoomId = messageEntity.getChatRoomEntity().getId();

        return new Message(
                messageEntity.getId(),
                chatRoomId, // ChatRoom 객체 대신 ID 참조
                userName,
                messageEntity.getMessage(),
                messageEntity.getRegisteredAt()
        );
    }
}