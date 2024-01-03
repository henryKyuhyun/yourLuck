package com.yourLuck.yourLuck.model;

import com.yourLuck.yourLuck.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class User {

    private Integer id;
    private String userName;
    private String password;
    private String nation;
    private LocalDateTime birthOfDayAndTime;
    private BloodType bloodType;
    private Gender gender;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static User fromEntity(UserEntity userEntity){
        return new User(
                userEntity.getId(),
                userEntity.getUserName(),
                userEntity.getPassword(),
                userEntity.getNation(),
                userEntity.getBirthOfDayAndTime(),
                userEntity.getBloodType(),
                userEntity.getGender(),
                userEntity.getRegisteredAt(),
                userEntity.getUpdatedAt(),
                userEntity.getDeletedAt()
        );
    }
}
