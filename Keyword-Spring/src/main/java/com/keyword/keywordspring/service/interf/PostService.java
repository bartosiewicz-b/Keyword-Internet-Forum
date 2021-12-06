package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.CreatePostRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;
import com.keyword.keywordspring.model.AppUser;

import java.util.List;

public interface PostService {

    void createPost(AppUser user, CreatePostRequest request);

    List<PostDto> getPosts(Integer page, String name);

    PostDto getPost(Long id);

    void editPost(AppUser user, EditPostRequest request);

    void deletePost(AppUser user, Long id);
}
