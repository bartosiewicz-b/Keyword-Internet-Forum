package com.keyword.keywordspring.service;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.request.CreateGroupRequest;
import com.keyword.keywordspring.mapper.interf.GroupMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.repository.GroupRepository;
import com.keyword.keywordspring.service.interf.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    @Override
    public void createGroup(AppUser user, CreateGroupRequest request) {

        ForumGroup forumGroup = ForumGroup.builder()
                .owner(user)
                .groupName(request.getGroupName())
                .description(request.getDescription())
                .dateCreated(new Date(System.currentTimeMillis()))
                .build();

        groupRepository.save(forumGroup);
    }

    @Override
    public List<GroupDto> getGroups(Integer page, String name) {

        return (name == null ?
                groupRepository.findAll(PageRequest.of(page, 10)) :
                groupRepository.findByGroupNameLike("%"+name+"%", PageRequest.of(page, 10)))
                        .stream()
                        .map(groupMapper::mapToDto)
                        .collect(Collectors.toList());
    }

    @Override
    public boolean isGroupNameTaken(String name) {
        return groupRepository.findByGroupName(name).isPresent();
    }
}
