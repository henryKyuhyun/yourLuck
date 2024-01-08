package com.yourLuck.yourLuck.controller.request;

import com.yourLuck.yourLuck.model.BloodType;
import com.yourLuck.yourLuck.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UserJoinRequest {

    private String name;
    private String password;

    private String nation;
    private LocalDateTime birthOfDayAndTime;


    private BloodType bloodType;

    private Gender gender;


}
