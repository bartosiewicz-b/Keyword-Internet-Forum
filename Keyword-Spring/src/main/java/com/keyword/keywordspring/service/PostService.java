package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.request.CreatePostRequest;
import com.keyword.keywordspring.model.AppUser;

public interface PostService {

    void createPost(AppUser user, CreatePostRequest request);
}
