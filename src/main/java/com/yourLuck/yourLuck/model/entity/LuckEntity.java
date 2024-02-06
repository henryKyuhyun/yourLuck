package com.yourLuck.yourLuck.model.entity;

import com.yourLuck.yourLuck.model.Fortune;
import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table
@Getter @Setter
public class LuckEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private Fortune fortune;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @PrePersist
    void registeredAt(){
        this.registeredAt = Timestamp.from(Instant.now());
    }

    public static LuckEntity of(UserEntity userEntity,Fortune fortune){
        LuckEntity entity = new LuckEntity();
        entity.setUser(userEntity);
        entity.setFortune(fortune);
        return entity;
    }
}
