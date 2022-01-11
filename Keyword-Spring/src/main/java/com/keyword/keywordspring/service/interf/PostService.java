package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.CreatePostRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;
import com.keyword.keywordspring.model.AppUser;

import java.util.List;

public interface PostService {

    Long createPost(AppUser user, CreatePostRequest request);

    List<PostDto> getPosts(Integer page, String name, String groupId, AppUser user);

    PostDto getPost(Long id, AppUser user);

    Long editPost(AppUser user, EditPostRequest request);

    void upvote(AppUser user, Long postId);
    void downvote(AppUser user, Long postId);

    void deletePost(AppUser user, Long id);
}
