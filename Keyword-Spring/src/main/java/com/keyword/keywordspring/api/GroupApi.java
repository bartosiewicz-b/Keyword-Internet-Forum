package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.request.CreateGroupRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.GroupService;
import com.keyword.keywordspring.service.interf.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @GetMapping("/get")
    public ResponseEntity<List<GroupDto>> getGroups(@RequestParam Integer page,
                                                    @RequestParam(required = false) String name) {

        try {
            return ResponseEntity.ok().body(groupService.getGroups(page, name));
        } catch (Exception ignored) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/validate-new/group-name")
    public ResponseEntity<Boolean> validateNewGroupName(@RequestBody String name) {

        if(groupService.isGroupNameTaken(name))
            return ResponseEntity.badRequest().body(false);
        else
            return ResponseEntity.ok().body(true);
    }
}
