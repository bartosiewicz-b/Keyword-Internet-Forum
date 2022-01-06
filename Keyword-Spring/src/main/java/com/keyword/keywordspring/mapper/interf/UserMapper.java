package com.keyword.keywordspring.mapper.interf;

import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.model.AppUser;

public interface UserMapper {
    UserDto mapToDto(AppUser user);
}
