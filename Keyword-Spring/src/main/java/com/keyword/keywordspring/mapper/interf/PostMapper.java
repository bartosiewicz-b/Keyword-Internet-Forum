package com.keyword.keywordspring.mapper.interf;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.Post;

public interface PostMapper {

    PostDto mapToDto(Post model, AppUser user);
}
