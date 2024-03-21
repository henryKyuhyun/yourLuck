package com.yourLuck.yourLuck.controller.response;

import com.yourLuck.yourLuck.model.BloodType;
import com.yourLuck.yourLuck.model.Gender;
import com.yourLuck.yourLuck.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class UserLoginDTO {
    private Integer id;
    private String username;
    private String nation;
    private LocalDateTime birthOfDayAndTime;
    private BloodType bloodType;
    private Gender gender;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static UserLoginDTO fromUser(User user) {
        return new UserLoginDTO(
                user.getId(),
                user.getUsername(),
                user.getNation(),
                user.getBirthOfDayAndTime(),
                user.getBloodType(),
                user.getGender(),
                user.getRegisteredAt(),
                user.getUpdatedAt(),
                user.getDeletedAt()
        );
    }
}

