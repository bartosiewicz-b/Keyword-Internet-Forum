package com.keyword.keywordspring.mapper;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.mapper.interf.GroupMapper;
import com.keyword.keywordspring.mapper.interf.UserMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.repository.GroupSubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GroupMapperImpl implements GroupMapper {

    private final UserMapper userMapper;
    private final GroupSubscriptionRepository subscriptionRepository;

    @Override
    public GroupDto mapToDto(ForumGroup group, AppUser user) {

        return GroupDto.builder()
                .id(group.getId())
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .subscriptions(group.getSubscriptions())
                .isSubscribed(null != user && (subscriptionRepository.findByUserAndGroup(user, group).isPresent()))
                .owner(group.getOwner().getUsername())
                .moderators(group.getModerators().stream().map(AppUser::getUsername).collect(Collectors.toList()))
                .build();
    }
}
