package com.yourLuck.yourLuck.model;

import com.yourLuck.yourLuck.model.entity.LuckEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Luck {

    private Integer id;
    private String  userName;
    private Fortune fortune;
    private String fortuneMessage;
    private Timestamp registeredAt;

    public static Luck fromEntity(LuckEntity luckEntity){
        return new Luck(
                luckEntity.getId(),
                luckEntity.getUser().getUserName(),
                luckEntity.getFortune(),
                luckEntity.getFortune().getMessage(),
                luckEntity.getRegisteredAt()
        );
    }
}
