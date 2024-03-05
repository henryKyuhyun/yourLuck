package com.yourLuck.yourLuck.repository;

import com.yourLuck.yourLuck.model.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
    List<MessageEntity> findByChatRoomEntity_Id(Integer chatRoomId);

}
