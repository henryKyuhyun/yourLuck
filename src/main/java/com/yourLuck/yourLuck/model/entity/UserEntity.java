package com.yourLuck.yourLuck.model.entity;

import com.fasterxml.jackson.annotation.*;
import com.yourLuck.yourLuck.model.BloodType;
import com.yourLuck.yourLuck.model.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Getter @Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "userName")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "nation")
    private String nation;
    @Column (name = "birthOfDayAndTime")
    private LocalDateTime birthOfDayAndTime;
    @Column(name = "bloodType")
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @JsonIgnore
    @Column(name = "registered_at")
    private Timestamp registeredAt;
    @JsonIgnore
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @JsonIgnore
    @Column(name = "removed_at")
    private Timestamp deletedAt;


    //@JsonManagedReference
    @ManyToMany(mappedBy = "users")
    private Set<ChatRoomEntity> chatRooms = new HashSet<>();
    // Jackson이 JSON으로부터 객체를 생성할 때 사용할 기본 생성자

    @PrePersist
    void registeredAt(){
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updateAt(){
        this.updatedAt = Timestamp.from(Instant.now());
    }

    @PreRemove
    void deletedAt(){ this.deletedAt = Timestamp.from(Instant.now());}

    // @JsonCreator를 사용하여 JSON으로부터 객체를 생성할 때 사용할 명시적 생성자를 정의합니다.
    @JsonCreator
    public static UserEntity of(
            @JsonProperty("userName") String userName,
            @JsonProperty("password") String password,
            @JsonProperty("nation") String nation,
            @JsonProperty("birthOfDayAndTime") LocalDateTime birthOfDayAndTime,
            @JsonProperty("bloodType") BloodType bloodType,
            @JsonProperty("gender") Gender gender
)
    {

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        userEntity.setNation(nation);
        userEntity.setBirthOfDayAndTime(birthOfDayAndTime);
        userEntity.setBloodType(bloodType);
        userEntity.setGender(gender);

        return userEntity;

}

//    public static UserEntity of(String userName, String password ,String nation, LocalDateTime birthOfDayAndTime, BloodType bloodType, Gender gender) {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUserName(userName);
//        userEntity.setPassword(password);
//        userEntity.setNation(nation);
//        userEntity.setBirthOfDayAndTime(birthOfDayAndTime);
//        userEntity.setBloodType(bloodType);
//        userEntity.setGender(gender);
//
//        return userEntity;
//    }

}

