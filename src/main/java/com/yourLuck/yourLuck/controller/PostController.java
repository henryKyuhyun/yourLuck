package com.yourLuck.yourLuck.controller;

import com.yourLuck.yourLuck.controller.request.PostCreateRequest;
import com.yourLuck.yourLuck.controller.request.PostModifyRequest;
import com.yourLuck.yourLuck.controller.response.PostResponse;
import com.yourLuck.yourLuck.controller.response.Response;
import com.yourLuck.yourLuck.model.Post;
import com.yourLuck.yourLuck.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

//    @PostMapping
//    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication){
//        postService.create(request.getTitle(), request.getContent(), authentication.getName());
//        return Response.success();
//    }
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
}
