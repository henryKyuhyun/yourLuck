package com.yourLuck.yourLuck.service;

import com.yourLuck.yourLuck.controller.response.Response;
import com.yourLuck.yourLuck.exception.ErrorCode;
import com.yourLuck.yourLuck.exception.LuckApplicationException;
import com.yourLuck.yourLuck.model.Comment;
import com.yourLuck.yourLuck.model.CommentStatus;
import com.yourLuck.yourLuck.model.Post;
import com.yourLuck.yourLuck.model.entity.CommentEntity;
import com.yourLuck.yourLuck.model.entity.LikeEntity;
import com.yourLuck.yourLuck.model.entity.PostEntity;
import com.yourLuck.yourLuck.model.entity.UserEntity;
import com.yourLuck.yourLuck.repository.CommentEntityRepository;
import com.yourLuck.yourLuck.repository.LikeEntityRepository;
import com.yourLuck.yourLuck.repository.PostEntityRepository;
import com.yourLuck.yourLuck.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;

    private UserEntity getUserEntityOrException(String userName) {
        return userEntityRepository.findByUserName(userName).orElseThrow(()->
                new LuckApplicationException(ErrorCode.USER_NOT_FOUNDED, String.format("%s not founded", userName)));
    }

    private PostEntity getPostEntityOrException(Integer postId) {
        return postEntityRepository.findById(postId).orElseThrow(() ->
                new LuckApplicationException(ErrorCode.USER_NOT_FOUNDED, String.format("%s not founded", postId)));
    }

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
            throw new LuckApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no comment", userName,postId));
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

    @Transactional
    public Comment comment(Integer postId, String userName, String comment,CommentEntity parent) {

        UserEntity userEntity = getUserEntityOrException(userName);
        PostEntity postEntity = getPostEntityOrException(postId);
        CommentEntity commentEntity = commentEntityRepository.save(CommentEntity.of(userEntity,postEntity,comment,parent));

        return Comment.fromEntity(commentEntity);
    }

    @Transactional
    public Page<Comment> getComments(Integer postId, Pageable pageable) {
        PostEntity postEntity = getPostEntityOrException(postId);
        return commentEntityRepository.findAllByPost(postEntity,pageable).map(Comment::fromEntity);
    }

    @Transactional
    public void deleteComment(Integer commentId, Authentication authentication){
        CommentEntity commentEntity = commentEntityRepository.findById(commentId)
                .orElseThrow(() -> new LuckApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s already like post %d", commentId)));
//        본인만 삭제가능
        String authenticatedUserName = authentication.getName();
        if(!commentEntity.getUser().getUserName().equals(authenticatedUserName)) {
            throw new LuckApplicationException(ErrorCode.INVALID_PERMISSION, "본인이 작성한 댓글만 삭제할 수 있습니다.");
        }
//        댓글 내용 변경
        commentEntity.delete();
//        댓글 상태를 Deleted로 변경
        commentEntity.setStatus(CommentStatus.DELETED);
//      댓글의 삭제 시간기록
        commentEntity.setDeletedAt(Timestamp.from(Instant.now()));
//      자식 댓글들의 부모를 Null로 설정
        for(CommentEntity child : commentEntity.getChildren()){
            child.setParent(null);
        }
        // 댓글 상태 변경 저장
        commentEntityRepository.save(commentEntity);
        // 댓글을 DB에서 삭제합니다.
        commentEntityRepository.deleteById(commentId);
    }



    }

