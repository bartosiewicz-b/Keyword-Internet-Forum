package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.CreateCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.dto.request.IdRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.CommentService;
import com.keyword.keywordspring.service.interf.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentApi {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<String> createComment(@RequestHeader("Authorization") String token,
                                           @RequestBody CreateCommentRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        try {
            commentService.addComment(user, request);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<CommentDto>> getComments(@RequestParam Integer page) {

        try {
            return ResponseEntity.ok().body(commentService.getComments(page));
        } catch(Exception ignored) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editComment(@RequestHeader("Authorization") String token,
                                         @RequestBody EditCommentRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        try{
            commentService.editComment(user, request);
                    return ResponseEntity.ok().build();
        } catch (Exception ignored) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteComment(@RequestHeader("Authorization") String token,
                                              @RequestBody IdRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        try{
            commentService.deleteComment(user, request.getId());
            return ResponseEntity.ok().build();
        } catch (Exception ignored) {
            return ResponseEntity.badRequest().build();
        }
    }
}
