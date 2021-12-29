package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.CreateCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.VoteType;

import java.util.List;

public interface CommentService {

    CommentDto addComment(AppUser user, CreateCommentRequest request);

    List<CommentDto> getComments(Long postId, AppUser user);

    void editComment(AppUser user, EditCommentRequest request);

    void vote(AppUser user, Long commentId, VoteType voteType);

    void deleteComment(AppUser user, Long id);
}
