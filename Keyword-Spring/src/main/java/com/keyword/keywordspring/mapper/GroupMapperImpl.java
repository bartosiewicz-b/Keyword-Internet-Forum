package com.keyword.keywordspring.mapper;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.exception.GroupDoesNotExistException;
import com.keyword.keywordspring.mapper.interf.GroupMapper;
import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class GroupMapperImpl implements GroupMapper {

    private final GroupRepository groupRepository;

    @Override
    public GroupDto mapToDto(ForumGroup group) {
        return GroupDto.builder()
                .id(group.getId())
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .build();
    }

    @Override
    public ForumGroup mapToModel(GroupDto dto) {
        Optional<ForumGroup> group = groupRepository.findById(dto.getId());

        if(group.isPresent())
            return group.get();

        throw new GroupDoesNotExistException(dto.getId());
    }
}
