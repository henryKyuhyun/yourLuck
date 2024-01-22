package com.yourLuck.yourLuck.service;

import com.yourLuck.yourLuck.model.BloodType;
import com.yourLuck.yourLuck.model.Fortune;
import com.yourLuck.yourLuck.model.Gender;
import com.yourLuck.yourLuck.model.User;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import com.yourLuck.yourLuck.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.IntStream;

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
public int registrationDateToNumber(User user) {
    Timestamp timestamp = user.getRegisteredAt();
    LocalDate registrationDate = timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    return registrationDate.getMonthValue() * 100 + registrationDate.getDayOfMonth();
}

    public String calculateFortune(User user) {
        int nameLength = user.getUsername().length();
        int bloodTypeNumber = bloodTypeToNumber(user.getBloodType());
        int genderNumber = genderToNumber(user.getGender());
        int registrationNumber = registrationDateToNumber(user);
        int birthNumber = user.getBirthOfDayAndTime().getDayOfYear();

        int totalNumber = nameLength + bloodTypeNumber + genderNumber + registrationNumber + birthNumber;
        int remainder = totalNumber % Fortune.values().length;

        return Fortune.values()[remainder].getMessage();
    }

    private int calculateTotalNumber(User user) {
        int nameLength = user.getUsername().length();
        int bloodTypeNumber = bloodTypeToNumber(user.getBloodType());
        int genderNumber = genderToNumber(user.getGender());
        int registrationNumber = registrationDateToNumber(user);
        int birthNumber = user.getBirthOfDayAndTime().getDayOfYear();

        return nameLength + bloodTypeNumber + genderNumber + registrationNumber + birthNumber;
    }

    public int[] generateLottoNumber(User user){
        int totalNumber = calculateTotalNumber(user);
        Random random = new Random(totalNumber);

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
//    public int[] generateLottoNumber(User user) {
//        int totalNumber = calculateTotalNumber(user);
//        Random random = new Random(totalNumber);
//
//        int[] count = new int[46];
//        for(long i = 0; i < 10000000000L; i++){
//            count[random.nextInt(45) + 1]++;
//        }
//        int[] result = new int[6];
//        for(int i=0; i< 6; i++){
//            int maxIndex = 0;
//            for(int j = 1; j<count.length; j++){
//                if(count[j] > count[maxIndex]){
//                    maxIndex = j;
//                }
//            }
//            result[i] = maxIndex;
//            count[maxIndex] = -1;
//        }
//        Arrays.sort(result);
//        return result;
//    }

}