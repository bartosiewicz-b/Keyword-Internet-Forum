package com.keyword.keywordspring.mapper;

import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.mapper.interf.UserMapper;
import com.keyword.keywordspring.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto mapToDto(AppUser user) {
        return UserDto.builder()
                .username(user.getUsername())
                .dateCreated(user.getDateCreated())
                .comments(user.getNrOfComments())
                .posts(user.getNrOfPosts())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
