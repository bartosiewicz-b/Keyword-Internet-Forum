package com.keyword.keywordspring.mapper.interf;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ForumGroup;

public interface GroupMapper {

    GroupDto mapToDto(ForumGroup group, AppUser user);
    ForumGroup mapToModel(GroupDto dto);
}
