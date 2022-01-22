package com.keyword.keywordspring.service.interf;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.dto.request.AddGroupRequest;
import com.keyword.keywordspring.dto.request.EditGroupRequest;

import java.util.List;

public interface GroupService {

    GroupDto add(String token, AddGroupRequest request);

    List<GroupDto> getAll(String token, Integer page, String keyword);
    int getCount(String keyword);
    GroupDto get(String token, String id);

    GroupDto edit(String token, EditGroupRequest request);

    void delete(String token, String id);

    void transferOwnership(String token, String id, String newOwnerUsername);

    List<UserDto> getSubscribers(String id, String keyword);
    void subscribe(String token, String id);

    List<UserDto> getModerators(String id);
    boolean isModerator(String username, String id);
    void addModerator(String token, String moderatorUsername, String id);
    void deleteModerator(String token, String moderatorUsername, String id);

    boolean isGroupNameTaken(String name);
}
