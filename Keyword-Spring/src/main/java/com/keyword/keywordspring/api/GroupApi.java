package com.keyword.keywordspring.api;

import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.request.*;
import com.keyword.keywordspring.exception.UnexpectedProblemException;
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
@CrossOrigin("http://localhost:4200")
public class GroupApi {

    private final GroupService groupService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<String> createGroup(@RequestHeader("Authorization") String token,
                                              @RequestBody CreateGroupRequest request) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            return ResponseEntity.ok().body(groupService.createGroup(user, request));
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<GroupDto>> getGroups(@RequestHeader(value = "Authorization", required = false) String token,
                                                    @RequestParam Integer page,
                                                    @RequestParam(required = false) String name) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            return ResponseEntity.ok().body(groupService.getGroups(page, name, user));
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<GroupDto> getGroup(@RequestHeader(value = "Authorization", required = false) String token,
                                             @RequestParam String id) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            return ResponseEntity.ok().body(groupService.getGroup(id, user));
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<Void> editGroup(@RequestHeader("Authorization") String token,
                                            @RequestBody EditGroupRequest request) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            groupService.editGroup(user, request);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }

    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribeGroup(@RequestHeader("Authorization") String token,
                                               @RequestBody IdStringRequest request) {

        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            groupService.subscribeGroup(user, request.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @PostMapping("/validate-new/group-name")
    public ResponseEntity<Void> validateNewGroupName(@RequestBody NameRequest request) {

        if(null == request.getName() || groupService.isGroupNameTaken(request.getName()))
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteGroup(@RequestHeader("Authorization") String token,
                                            @RequestBody IdStringRequest request) {
        try {
            AppUser user = jwtUtil.getUserFromToken(token);

            groupService.deleteGroup(user, request.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }
}
