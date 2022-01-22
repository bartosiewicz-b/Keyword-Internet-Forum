package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.AddPostRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;

import java.util.List;

public interface PostService {

    PostDto add(String token, AddPostRequest request);

    List<PostDto> getAll(String token, String groupId, int page, String keyword);
    int getCount(String keyword, String groupId);
    PostDto get(String token, Long id);

    PostDto edit(String token, EditPostRequest request);

    void delete(String token, Long id);

    void upvote(String token, Long id);
    void downvote(String token, Long id);
}
