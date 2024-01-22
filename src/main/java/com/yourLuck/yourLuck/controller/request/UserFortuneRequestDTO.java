package com.yourLuck.yourLuck.controller.request;

import com.yourLuck.yourLuck.model.BloodType;
import com.yourLuck.yourLuck.model.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserFortuneRequestDTO {

    private String userName;
    private BloodType bloodType;
    private Gender gender;
    private LocalDate birthOfDayAndTime;


}
