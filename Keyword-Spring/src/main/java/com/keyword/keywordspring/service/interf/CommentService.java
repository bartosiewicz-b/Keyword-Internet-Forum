package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.CreateCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.model.AppUser;

import java.util.List;

public interface CommentService {

    void addComment(AppUser user, CreateCommentRequest request);

    List<CommentDto> getComments(Integer page);

    void editComment(AppUser user, EditCommentRequest request);

    void deleteComment(AppUser user, Long id);
}
