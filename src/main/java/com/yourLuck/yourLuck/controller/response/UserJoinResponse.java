package com.yourLuck.yourLuck.controller.response;

import com.yourLuck.yourLuck.model.BloodType;
import com.yourLuck.yourLuck.model.Gender;
import com.yourLuck.yourLuck.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

    private Integer id;
    private String userName;
    private String nation;

    private LocalDateTime birthOfDayAndTime;

    private BloodType bloodType;

    private Gender gender;

    public static UserJoinResponse fromUser(User user){
        return new UserJoinResponse(
                user.getId(),
                user.getUsername(),
                user.getNation(),
                user.getBirthOfDayAndTime(),
                user.getBloodType(),
                user.getGender()
        );
    }
}
