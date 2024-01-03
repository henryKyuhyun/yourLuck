package com.yourLuck.yourLuck.model.entity;

import com.yourLuck.yourLuck.model.BloodType;
import com.yourLuck.yourLuck.model.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @Column(name = "registered_at")
    private Timestamp registeredAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "removed_at")
    private Timestamp deletedAt;

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

    public static UserEntity of(String userName, String password ,String nation, LocalDateTime birthOfDayAndTime, BloodType bloodType, Gender gender) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        userEntity.setNation(nation);
        userEntity.setBirthOfDayAndTime(birthOfDayAndTime);
        userEntity.setBloodType(bloodType);
        userEntity.setGender(gender);

        return userEntity;
    }
}
