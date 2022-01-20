package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.dto.request.*;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.GroupService;
import com.keyword.keywordspring.service.interf.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/group")
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class GroupApi {

    private final GroupService groupService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<Void> createGroup(@RequestHeader("Authorization") String token,
                                              @RequestBody CreateGroupRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        groupService.createGroup(user, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<GroupDto>> getGroups(@RequestHeader(value = "Authorization", required = false) String token,
                                                    @RequestParam Integer page,
                                                    @RequestParam(required = false) String name) {

        AppUser user = jwtUtil.getUserFromToken(token);

        return ResponseEntity.ok().body(groupService.getGroups(page, name, user));
    }

    @GetMapping("/get-all-count")
    public ResponseEntity<Integer> getGroupsCount(@RequestParam(required = false) String name) {

        return ResponseEntity.ok().body(groupService.getGroupsCount(name));
    }

    @GetMapping("/get")
    public ResponseEntity<GroupDto> getGroup(@RequestHeader(value = "Authorization", required = false) String token,
                                             @RequestParam String id) {

        AppUser user = jwtUtil.getUserFromToken(token);

        return ResponseEntity.ok().body(groupService.getGroup(id, user));
    }

    @PostMapping("/edit")
    public ResponseEntity<Void> editGroup(@RequestHeader("Authorization") String token,
                                            @RequestBody EditGroupRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        groupService.editGroup(user, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer-ownership")
    public ResponseEntity<Void> transferOwnership(@RequestHeader("Authorization") String token,
                                                  @RequestBody SubscriberRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        groupService.transferOwnership(user, request.getGroupId(), request.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-subscribers")
    public ResponseEntity<List<UserDto>> getSubscribers(@RequestParam String groupId, @RequestParam String username) {

        return ResponseEntity.ok().body(groupService.getSubscribers(groupId, username));
    }

    @PostMapping("/add-moderator")
    public ResponseEntity<Void> addModerator(@RequestHeader("Authorization") String token,
                                             @RequestBody SubscriberRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        groupService.addModerator(user, request.getUsername(), request.getGroupId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete-moderator")
    public ResponseEntity<Void> deleteModerator(@RequestHeader("Authorization") String token,
                                                @RequestBody SubscriberRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        groupService.removeModerator(user, request.getUsername(), request.getGroupId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-moderators")
    public ResponseEntity<List<UserDto>> getModerators(@RequestParam String groupId) {

        return ResponseEntity.ok().body(groupService.getModerators(groupId));
    }

    @GetMapping("/is-moderator")
    public ResponseEntity<Boolean> isModerator(@RequestBody SubscriberRequest request) {

        return ResponseEntity.ok().body(groupService.isModerator(request.getUsername(), request.getGroupId()));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribeGroup(@RequestHeader("Authorization") String token,
                                               @RequestBody Map<String, String> request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        groupService.subscribeGroup(user, request.get("groupId"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-subscribed")
    public ResponseEntity<List<GroupDto>> getSubscribedGroups(@RequestHeader("Authorization") String token) {

        AppUser user = jwtUtil.getUserFromToken(token);

        return ResponseEntity.ok().body(groupService.getSubscribedGroups(user));
    }

    @PostMapping("/validate-new/group-name")
    public ResponseEntity<Void> validateNewGroupName(@RequestBody Map<String, String> request) {

        if(null == request.get("groupName") || groupService.isGroupNameTaken(request.get("groupName")))
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteGroup(@RequestHeader("Authorization") String token,
                                            @RequestBody Map<String, String> request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        groupService.deleteGroup(user, request.get("groupId"));
        return ResponseEntity.ok().build();
    }
}
