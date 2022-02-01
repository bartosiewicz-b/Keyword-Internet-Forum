package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.AddPostRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;
import com.keyword.keywordspring.service.interf.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class PostApi {

    private final PostService postService;

    @PostMapping("/add")
    public ResponseEntity<PostDto> addPost(@RequestHeader("Authorization") String token,
                                           @RequestBody AddPostRequest request) {

        return ResponseEntity.ok().body(postService.add(token, request));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestHeader(value = "Authorization", required = false) String token,
                                                     @RequestParam(required = false) String groupId,
                                                     @RequestParam Integer page,
                                                     @RequestParam(required = false) String keyword) {

        return ResponseEntity.ok().body(postService.getAll(token, groupId, page, keyword));
    }

    @GetMapping("/get-count")
    public ResponseEntity<Integer> getPostsCount(@RequestParam(required = false) String groupId,
                                                 @RequestParam(required = false) String keyword) {

        return ResponseEntity.ok().body(postService.getCount(keyword, groupId));
    }

    @GetMapping("/get")
    public ResponseEntity<PostDto> getPost(@RequestHeader(value = "Authorization", required = false) String token,
                                           @RequestParam Long postId) {

        return ResponseEntity.ok().body(postService.get(token, postId));
    }

    @PostMapping("/edit")
    public ResponseEntity<PostDto> editPost(@RequestHeader("Authorization") String token,
                                            @RequestBody EditPostRequest request) {

        return ResponseEntity.ok().body(postService.edit(token, request));
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deletePost(@RequestHeader("Authorization") String token,
                                           @RequestBody Long postId) {

        postService.delete(token, postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upvote")
    public ResponseEntity<Integer> upvotePost(@RequestHeader("Authorization") String token,
                                             @RequestBody Long postId) {

        return ResponseEntity.ok().body(postService.upvote(token, postId));
    }

    @PostMapping("/downvote")
    public ResponseEntity<Integer> downvotePost(@RequestHeader("Authorization") String token,
                                             @RequestBody Long postId) {

        return ResponseEntity.ok().body(postService.downvote(token, postId));
    }
}
