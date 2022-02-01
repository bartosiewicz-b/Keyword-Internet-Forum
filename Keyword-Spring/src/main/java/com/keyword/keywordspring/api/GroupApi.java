package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.dto.request.*;
import com.keyword.keywordspring.service.interf.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/group")
@AllArgsConstructor
public class GroupApi {

    private final GroupService groupService;

    @PostMapping("/add")
    public ResponseEntity<GroupDto> addGroup(@RequestHeader("Authorization") String token,
                                             @RequestBody AddGroupRequest request) {

        return ResponseEntity.ok().body(groupService.add(token, request));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<GroupDto>> getAllGroups(@RequestHeader(value = "Authorization", required = false) String token,
                                                       @RequestParam Integer page,
                                                       @RequestParam(required = false) String keyword) {

        return ResponseEntity.ok().body(groupService.getAll(token, page, keyword));
    }

    @GetMapping("/get-count")
    public ResponseEntity<Integer> getGroupsCount(@RequestParam(required = false) String keyword) {

        return ResponseEntity.ok().body(groupService.getCount(keyword));
    }

    @GetMapping("/get")
    public ResponseEntity<GroupDto> getGroup(@RequestHeader(value = "Authorization", required = false) String token,
                                             @RequestParam String groupId) {

        return ResponseEntity.ok().body(groupService.get(token, groupId));
    }

    @PostMapping("/edit")
    public ResponseEntity<GroupDto> editGroup(@RequestHeader("Authorization") String token,
                                              @RequestBody EditGroupRequest request) {

        return ResponseEntity.ok().body(groupService.edit(token, request));
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteGroup(@RequestHeader("Authorization") String token,
                                            @RequestBody String groupId) {

        groupService.delete(token, groupId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer-ownership")
    public ResponseEntity<Void> transferOwnership(@RequestHeader("Authorization") String token,
                                                  @RequestBody GroupUserRequest request) {

        groupService.transferOwnership(token, request.getGroupId(), request.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-subscribers")
    public ResponseEntity<List<UserDto>> getSubscribers(@RequestParam String groupId,
                                                        @RequestParam String keyword) {

        return ResponseEntity.ok().body(groupService.getSubscribers(groupId, keyword));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribeGroup(@RequestHeader("Authorization") String token,
                                               @RequestBody String groupId) {

        groupService.subscribe(token, groupId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-moderators")
    public ResponseEntity<List<UserDto>> getModerators(@RequestParam String groupId) {

        return ResponseEntity.ok().body(groupService.getModerators(groupId));
    }

    @GetMapping("/is-moderator")
    public ResponseEntity<Boolean> isModerator(@RequestBody GroupUserRequest request) {

        return ResponseEntity.ok().body(groupService.isModerator(request.getUsername(), request.getGroupId()));
    }

    @PostMapping("/add-moderator")
    public ResponseEntity<Void> addModerator(@RequestHeader("Authorization") String token,
                                             @RequestBody GroupUserRequest request) {

        groupService.addModerator(token, request.getUsername(), request.getGroupId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete-moderator")
    public ResponseEntity<Void> deleteModerator(@RequestHeader("Authorization") String token,
                                                @RequestBody GroupUserRequest request) {

        groupService.deleteModerator(token, request.getUsername(), request.getGroupId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate-new-group-name")
    public ResponseEntity<Void> validateNewGroupName(@RequestBody String groupName) {

        if(groupService.isGroupNameTaken(groupName))
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok().build();
    }
}
