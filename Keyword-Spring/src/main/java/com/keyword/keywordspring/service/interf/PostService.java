package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.CreatePostRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.VoteType;

import java.util.List;

public interface PostService {

    Long createPost(AppUser user, CreatePostRequest request);

    List<PostDto> getPosts(Integer page, String name, AppUser user);

    PostDto getPost(Long id, AppUser user);

    void editPost(AppUser user, EditPostRequest request);

    void vote(AppUser user, Long postId, VoteType voteType);

    void deletePost(AppUser user, Long id);
}
