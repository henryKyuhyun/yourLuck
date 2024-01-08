package com.yourLuck.yourLuck.repository;

import com.yourLuck.yourLuck.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity,Integer> {

    Optional<UserEntity> findByUserName(String userName);
}
