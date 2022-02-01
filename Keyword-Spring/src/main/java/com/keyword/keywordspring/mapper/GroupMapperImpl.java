package com.keyword.keywordspring.mapper;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.mapper.interf.GroupMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ForumGroup;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GroupMapperImpl implements GroupMapper {

    @Override
    public GroupDto mapToDto(ForumGroup group, AppUser user) {

        return GroupDto.builder()
                .id(group.getId())
                .groupName(group.getGroupName())
                .avatarUrl(group.getAvatarUrl())
                .description(group.getDescription())
                .subscriptions(group.getSubscriptions())
                .isSubscribed(!Objects.isNull(user) && user.getSubscribedGroups().contains(group))
                .owner(group.getOwner().getUsername())
                .moderators(group.getModerators().stream().map(AppUser::getUsername).collect(Collectors.toList()))
                .build();
    }
}
