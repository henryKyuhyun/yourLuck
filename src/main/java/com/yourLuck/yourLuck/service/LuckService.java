package com.yourLuck.yourLuck.service;

import com.yourLuck.yourLuck.model.*;
import com.yourLuck.yourLuck.model.entity.LuckEntity;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import com.yourLuck.yourLuck.repository.LuckEntityRepository;
import com.yourLuck.yourLuck.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LuckService {

    private final UserEntityRepository userEntityRepository;
    private final LuckEntityRepository luckEntityRepository;

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
public int registrationDateToNumber(User user) {
    Timestamp timestamp = user.getRegisteredAt();
    LocalDate registrationDate = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    return registrationDate.getMonthValue() * 100 + registrationDate.getDayOfMonth();
}

    public String calculateFortune(User user) {

        LocalDate today = LocalDate.now();
        int nameLength = user.getUsername().length();
        int bloodTypeNumber = bloodTypeToNumber(user.getBloodType());
        int genderNumber = genderToNumber(user.getGender());
        int registrationNumber = registrationDateToNumber(user);
        int birthNumber = user.getBirthOfDayAndTime().getDayOfYear();
/*
        100을 곱하는 이유는 각 날짜마자 고유한 숫자를 주기위함이다 예를들어 1월 2일과 2월 1일은 그냥 더하기만 하면 동일한 숫자지만
        곱하게된다면 102, 201 이런식으로다른숫자가 나오기떄문.
 */
        int todayNumber = today.getMonthValue() * 100 + today.getDayOfMonth();
        int totalNumber = nameLength + bloodTypeNumber + genderNumber + registrationNumber + birthNumber + todayNumber;
        int remainder = totalNumber % Fortune.values().length;

        Fortune fortune = Fortune.values()[remainder];
        String message = fortune.getMessage();
//        운세 db저장
        UserEntity userEntity = userEntityRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        LuckEntity luckEntity = LuckEntity.of(userEntity, fortune);
        luckEntityRepository.save(luckEntity);

//        return Fortune.values()[remainder].getMessage();
        return message;
    }


    private int calculateTotalNumber(User user) {
        int nameLength = user.getUsername().length();
        int bloodTypeNumber = bloodTypeToNumber(user.getBloodType());
        int genderNumber = genderToNumber(user.getGender());
        int registrationNumber = registrationDateToNumber(user);
        int birthNumber = user.getBirthOfDayAndTime().getDayOfYear();

        return nameLength + bloodTypeNumber + genderNumber + registrationNumber + birthNumber;
    }

    public int[] generateLottoNumber(){
        Random random = new Random();

        int[] count = new int[46];
        IntStream.range(0,100000000).forEach(i -> count[random.nextInt(45) + 1]++);

        int[] result = IntStream.range(0,count.length)
                .boxed()
                .sorted(Comparator.comparing(i -> -count[i]))
                .limit(6)
                .mapToInt(i -> i)
                .toArray();
        Arrays.sort(result);
        return result;
    }

    public List<Luck> getLuckHistory(Integer userId) {
        UserEntity userEntity = userEntityRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not founded"));
        return luckEntityRepository.findByUser(userEntity).stream()
                .map(Luck::fromEntity)
                .collect(Collectors.toList());
    }

}
