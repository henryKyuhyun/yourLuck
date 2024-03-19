package com.yourLuck.yourLuck.service;

import com.yourLuck.yourLuck.exception.ErrorCode;
import com.yourLuck.yourLuck.exception.LuckApplicationException;
import com.yourLuck.yourLuck.model.BloodType;
import com.yourLuck.yourLuck.model.Gender;
import com.yourLuck.yourLuck.model.User;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import com.yourLuck.yourLuck.repository.UserCacheRepository;
import com.yourLuck.yourLuck.repository.UserEntityRepository;
import com.yourLuck.yourLuck.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${jwt.token.secret}")
    private String secretKey;
    @Value("${jwt.refresh-expired}")
    private long refreshExpiredTimeMs;
    @Value("${jwt.access-expired}")
    private long accessExpiredTimeMs;

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserCacheRepository userCacheRepository;

    public User loadUserByUserName(String userName){
        return userCacheRepository.getUser(userName).orElseGet(() ->
                userEntityRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(() ->
                        new LuckApplicationException(ErrorCode.USER_NOT_FOUNDED,String.format("%s not founded", userName)))
            );
    }
    @Transactional
    public User join(String userName, String password, String nation, LocalDateTime birthOfDayAndTime,BloodType bloodType, Gender gender){
//        회원가입하려는 userName 으로 회원가입된 유저가 있는지 확인
        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new LuckApplicationException(ErrorCode.DUPLICATED_USER_NAME,String.format("%s is duplicated", userName));
        });

//        LocalDateTime birthOfDayAndTime = LocalDateTime.now();
//        회원가입 진행
        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName,encoder.encode(password), nation, birthOfDayAndTime, bloodType, gender));
        return User.fromEntity(userEntity);
    }


    @Transactional
    public String login(String userName, String password) {
//        회원가입여부체크
        User user = loadUserByUserName(userName);
//        캐싱에서 가져오기
        userCacheRepository.setUser(user);
//        비밀번호확인
        if(!encoder.matches(password,user.getPassword())){
            throw new LuckApplicationException(ErrorCode.INVALID_PASSWORD);
        }
//        TOKEN
        return JwtTokenUtils.generateToken(userName,secretKey,accessExpiredTimeMs);
    }
}
