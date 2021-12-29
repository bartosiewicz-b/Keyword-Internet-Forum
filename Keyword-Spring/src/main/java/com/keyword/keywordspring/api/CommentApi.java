package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.CreateCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.dto.request.IdRequest;
import com.keyword.keywordspring.exception.UnexpectedProblemException;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.VoteType;
import com.keyword.keywordspring.service.interf.CommentService;
import com.keyword.keywordspring.service.interf.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class CommentApi {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<CommentDto> createComment(@RequestHeader("Authorization") String token,
                                           @RequestBody CreateCommentRequest request) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            return ResponseEntity.ok().body(commentService.addComment(user, request));
        } catch(Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<CommentDto>> getComments(@RequestHeader(value = "Authorization", required = false) String token,
                                            @RequestParam Long postId) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            return ResponseEntity.ok().body(commentService.getComments(postId, user));
        } catch(Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<Void> editComment(@RequestHeader("Authorization") String token,
                                            @RequestBody EditCommentRequest request) {
        try{
            AppUser user = jwtUtil.getUserFromToken(token);

            commentService.editComment(user, request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/upvote")
    public ResponseEntity<Void> upvote(@RequestHeader("Authorization") String token,
            @RequestBody IdRequest request) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            commentService.vote(user, request.getId(), VoteType.UP);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/downvote")
    public ResponseEntity<Void> downvote(@RequestHeader("Authorization") String token,
                                       @RequestBody IdRequest request) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            commentService.vote(user, request.getId(), VoteType.DOWN);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteComment(@RequestHeader("Authorization") String token,
                                              @RequestBody IdRequest request) {
        try{
            AppUser user = jwtUtil.getUserFromToken(token);

            commentService.deleteComment(user, request.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }
}
