package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.request.CreateGroupRequest;
import com.keyword.keywordspring.dto.request.EditGroupRequest;
import com.keyword.keywordspring.exception.UnexpectedProblemException;
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

        try {
            groupService.createGroup(user, request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<GroupDto>> getGroups(@RequestHeader(value = "Authorization", required = false) String token,
                                                    @RequestParam Integer page,
                                                    @RequestParam(required = false) String name) {

        AppUser user = jwtUtil.getUserFromToken(token);

        try {
            return ResponseEntity.ok().body(groupService.getGroups(page, name, user));
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<GroupDto> getGroup(@RequestHeader(value = "Authorization", required = false) String token,
                                             @RequestParam String id) {

        AppUser user = jwtUtil.getUserFromToken(token);

        try {
            return ResponseEntity.ok().body(groupService.getGroup(id, user));
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<Void> editGroup(@RequestHeader("Authorization") String token,
                                            @RequestBody EditGroupRequest request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        try {
            groupService.editGroup(user, request);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }

    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribeGroup(@RequestHeader("Authorization") String token,
                                               @RequestBody Map<String, String> request) {

        AppUser user = jwtUtil.getUserFromToken(token);

        try {
            groupService.subscribeGroup(user, request.get("groupId"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
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

        try {
            groupService.deleteGroup(user, request.get("groupId"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }
}
