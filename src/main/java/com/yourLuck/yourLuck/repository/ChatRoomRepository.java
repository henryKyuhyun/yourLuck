package com.yourLuck.yourLuck.repository;

import com.yourLuck.yourLuck.model.entity.ChatRoomEntity;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Integer> {

    Page<ChatRoomEntity> findAllByUser(UserEntity entity, Pageable pageable);
    Optional<ChatRoomEntity> findByIdOrChatRoomName(Integer id, String chatRoomName);
    Optional<ChatRoomEntity> findByChatRoomName(String chatRoomName);

}
