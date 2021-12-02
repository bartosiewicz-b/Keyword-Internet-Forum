package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.request.CreateGroupRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.GroupService;
import com.keyword.keywordspring.service.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/group")
@AllArgsConstructor
public class GroupApi {

    private final GroupService groupService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<String> createGroup(@RequestHeader("Authorization") String token,
                                              @RequestBody CreateGroupRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        try {
            groupService.createGroup(user, request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validate-new/group-name")
    public ResponseEntity<String> validateNewEmail(@RequestBody String name) {

        if(groupService.isGroupNameTaken(name))
            return ResponseEntity.badRequest().body("This group name is already taken.");
        else
            return ResponseEntity.ok().build();
    }
}
