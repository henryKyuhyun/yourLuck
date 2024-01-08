package com.yourLuck.yourLuck.service;

import com.yourLuck.yourLuck.model.BloodType;
import com.yourLuck.yourLuck.model.Fortune;
import com.yourLuck.yourLuck.model.Gender;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import com.yourLuck.yourLuck.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class LuckService {

    private final UserEntityRepository userEntityRepository;

//    혈핵형 -> 숫자
    public int bloodTypeToNumber(BloodType bloodType) {
        return bloodType.ordinal() + 1;
//        Ordinal 은 Enum 상수의 순서를 반환하는 메서드로 상수가 선언된 순서에 따라 0부터 시작하는 숫자를 반환.
    }
//  성별  -> 숫자
    public int genderToNumber(Gender gender) {
        return gender == Gender.MALE ? 1 : 2;
    }

//    오늘의 날짜 -> 숫자
public int registrationDateToNumber(UserEntity userEntity) {
    Timestamp timestamp = userEntity.getRegisteredAt();
    LocalDate registrationDate = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    return registrationDate.getMonthValue() * 100 + registrationDate.getDayOfMonth();
}

    public String calculateFortune(UserEntity userEntity) {
        int nameLength = userEntity.getUserName().length();
        int bloodTypeNumber = bloodTypeToNumber(userEntity.getBloodType());
        int genderNumber = genderToNumber(userEntity.getGender());
        int todayNumber = registrationDateToNumber(userEntity);
        int birthNumber = userEntity.getBirthOfDayAndTime().getDayOfYear();
        int registrationNumber = registrationDateToNumber(userEntity);

        int totalNumber = nameLength + bloodTypeNumber + genderNumber + todayNumber + birthNumber + registrationNumber;
        int remainder = totalNumber % Fortune.values().length;

        return Fortune.values()[remainder].getMessage();
    }
}
