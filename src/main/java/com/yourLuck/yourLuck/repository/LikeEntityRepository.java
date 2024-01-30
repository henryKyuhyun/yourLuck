package com.yourLuck.yourLuck.repository;

import com.yourLuck.yourLuck.model.entity.LikeEntity;
import com.yourLuck.yourLuck.model.entity.PostEntity;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {

//    Optional 은 반환값이 NUll일 가능성이 있는경우 사용한다. 좋아요같은경우는 좋아요가 안달린 포스트가 있을경우악 있기때문에 Optional 을 이용!
    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);

    long countByPost(PostEntity post);

}
