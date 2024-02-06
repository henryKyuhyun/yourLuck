package com.yourLuck.yourLuck.repository;

import com.yourLuck.yourLuck.model.entity.LuckEntity;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LuckEntityRepository extends JpaRepository<LuckEntity,Integer> {

    List<LuckEntity> findByUser(UserEntity user);
}
