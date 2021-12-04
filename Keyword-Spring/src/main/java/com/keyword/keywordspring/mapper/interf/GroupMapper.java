package com.keyword.keywordspring.mapper.interf;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.model.ForumGroup;

public interface GroupMapper {

    GroupDto mapToDto(ForumGroup group);
    ForumGroup mapToModel(GroupDto dto);
}
