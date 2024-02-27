package com.yourLuck.yourLuck.controller;

import com.yourLuck.yourLuck.controller.request.PostCommentRequest;
import com.yourLuck.yourLuck.controller.request.PostCreateRequest;
import com.yourLuck.yourLuck.controller.request.PostModifyRequest;
import com.yourLuck.yourLuck.controller.response.CommentResponse;
import com.yourLuck.yourLuck.controller.response.PostResponse;
import com.yourLuck.yourLuck.controller.response.Response;
import com.yourLuck.yourLuck.exception.ErrorCode;
import com.yourLuck.yourLuck.exception.LuckApplicationException;
import com.yourLuck.yourLuck.model.Comment;
import com.yourLuck.yourLuck.model.Post;
import com.yourLuck.yourLuck.model.entity.CommentEntity;
import com.yourLuck.yourLuck.repository.CommentEntityRepository;
import com.yourLuck.yourLuck.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentEntityRepository commentEntityRepository;

    @PostMapping
    public Response<Post> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        Post post = postService.create(request.getTitle(), request.getContent(), authentication.getName());
        return Response.success(post);
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication) {
        Post post = postService.modify(request.getTitle(), request.getContent(), authentication.getName(), postId);
        return Response.success(PostResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);
        return Response.success();
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable Integer postId, Authentication authentication) {
        postService.like(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    public Response<Long> likeCount(@PathVariable Integer postId) {
        return Response.success(postService.likeCount(postId));
    }
    
@PostMapping("/{postId}/comments")
public Response<Comment> comment(@PathVariable Integer postId, @RequestBody PostCommentRequest request, Authentication authentication) {
    CommentEntity parent = null;
    if(request.getParentId() != null){
        parent = commentEntityRepository.findById(request.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid parentId: " + request.getParentId()));
    }
    Comment comment = postService.comment(postId,
            authentication.getName(),
            request.getComment(),
            parent);
    return Response.success(comment);
}

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> getComment(@PathVariable Integer postId, Pageable pageable) {
            return Response.success(postService.getComments(postId, pageable).map(CommentResponse::fromComment));
    }

    @DeleteMapping("/{postId}/comments")
    public Response<Void> deleteComment(@PathVariable Integer postId, Authentication authentication) {
        postService.deleteComment(postId, authentication);
        return Response.success();
    }
}
