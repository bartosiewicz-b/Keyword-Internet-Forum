package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.AddCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;

import java.util.List;

public interface CommentService {

    CommentDto add(String token, AddCommentRequest request);

    List<CommentDto> getAll(String token, Long postId);

    CommentDto edit(String token, EditCommentRequest request);

    void delete(String token, Long id);

    int upvote(String token, Long id);
    int downvote(String token, Long id);


}
