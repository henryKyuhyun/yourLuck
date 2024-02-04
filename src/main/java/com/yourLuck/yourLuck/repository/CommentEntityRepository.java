package com.yourLuck.yourLuck.repository;

import com.yourLuck.yourLuck.model.Comment;
import com.yourLuck.yourLuck.model.entity.CommentEntity;
import com.yourLuck.yourLuck.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentEntityRepository extends JpaRepository<CommentEntity, Integer>{

    Page<CommentEntity> findAllByPost(PostEntity postEntity, Pageable pageable);
    Optional<CommentEntity> findById(Integer Id);
}
