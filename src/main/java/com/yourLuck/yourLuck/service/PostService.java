package com.yourLuck.yourLuck.service;

import com.yourLuck.yourLuck.exception.ErrorCode;
import com.yourLuck.yourLuck.exception.LuckApplicationException;
import com.yourLuck.yourLuck.model.Post;
import com.yourLuck.yourLuck.model.entity.LikeEntity;
import com.yourLuck.yourLuck.model.entity.PostEntity;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import com.yourLuck.yourLuck.repository.LikeEntityRepository;
import com.yourLuck.yourLuck.repository.PostEntityRepository;
import com.yourLuck.yourLuck.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final LikeEntityRepository likeEntityRepository;

    private UserEntity getUserEntityOrException(String userName) {
        return userEntityRepository.findByUserName(userName).orElseThrow(()->
                new LuckApplicationException(ErrorCode.USER_NOT_FOUNDED, String.format("%s not founded", userName)));
    }

    private PostEntity getPostEntityOrException(Integer postId) {
        return postEntityRepository.findById(postId).orElseThrow(() ->
                new LuckApplicationException(ErrorCode.USER_NOT_FOUNDED, String.format("%s not founded", postId)));
    }
//    @Transactional
//    public void create(String title, String content, String userName) {
//        UserEntity userEntity = getUserEntityOrException(userName);
//        postEntityRepository.save(PostEntity.of(title,content,userEntity));
//    }
    @Transactional
    public Post create(String title, String content, String userName) {
        UserEntity userEntity = getUserEntityOrException(userName);
        PostEntity postEntity = postEntityRepository.save(PostEntity.of(title, content, userEntity));
        return Post.fromEntity(postEntity);
    }

    @Transactional
    public Post modify(String title, String content, String userName,Integer postId) {
        UserEntity userEntity = getUserEntityOrException(userName);
        PostEntity postEntity = getPostEntityOrException(postId);

//        post permission
        if(postEntity.getUser() != userEntity) {
            throw new LuckApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName,postId));
        }
        postEntity.setTitle(title);
        postEntity.setContent(content);
        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String userName, Integer postId) {
        UserEntity userEntity = getUserEntityOrException(userName);
        PostEntity postEntity = getPostEntityOrException(postId);
//        Post permission
        if(postEntity.getUser() != userEntity){
            throw new LuckApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName,postId));
        }
//        TODO : 나중에 좋아요나 댓글작업이 생길경우 순서를 잘정해함, 좋아요 -> 커멘트 -> 포스트가삭제되는식
        postEntityRepository.delete(postEntity);
    }

    @Transactional
    public void like(Integer postId, String userName) {
        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserEntityOrException(userName);

//        Check liked -> throw
        likeEntityRepository.findByUserAndPost(userEntity,postEntity).ifPresent(it -> {
            throw new LuckApplicationException(ErrorCode.ALREADY_LIKED,String.format("%s already like post %d", userName,postId));
        });

//        like save
        likeEntityRepository.save(LikeEntity.of(userEntity,postEntity));
//        TODO : 알람기능 개발
    }

    public long likeCount(Integer postId) {
        PostEntity postEntity = getPostEntityOrException(postId);
        return likeEntityRepository.countByPost(postEntity);
    }



    }

