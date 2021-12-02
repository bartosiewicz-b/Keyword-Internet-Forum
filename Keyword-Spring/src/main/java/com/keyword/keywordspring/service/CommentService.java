package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.request.CreateCommentRequest;
import com.keyword.keywordspring.model.AppUser;

public interface CommentService {

    void addComment(AppUser user, CreateCommentRequest request);
}
