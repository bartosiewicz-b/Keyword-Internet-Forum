package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.AddCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.service.interf.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentApi {

    private final CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<CommentDto> addComment(@RequestHeader("Authorization") String token,
                                                 @RequestBody AddCommentRequest request) {

        return ResponseEntity.ok().body(commentService.add(token, request));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<CommentDto>> getAllComments(@RequestHeader(value = "Authorization", required = false) String token,
                                                           @RequestParam Long postId) {

        return ResponseEntity.ok().body(commentService.getAll(token, postId));
    }

    @PostMapping("/edit")
    public ResponseEntity<CommentDto> editComment(@RequestHeader("Authorization") String token,
                                            @RequestBody EditCommentRequest request) {

        return ResponseEntity.ok().body(commentService.edit(token, request));
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteComment(@RequestHeader("Authorization") String token,
                                              @RequestBody Long commentId) {

        commentService.delete(token, commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upvote")
    public ResponseEntity<Integer> upvoteComment(@RequestHeader("Authorization") String token,
                                              @RequestBody Long commentId) {

        return ResponseEntity.ok().body(commentService.upvote(token, commentId));
    }

    @PostMapping("/downvote")
    public ResponseEntity<Integer> downvoteComment(@RequestHeader("Authorization") String token,
                                                @RequestBody Long commentId) {

        return ResponseEntity.ok().body(commentService.downvote(token, commentId));
    }


}
