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


@RestController
@RequestMapping("/group")
@AllArgsConstructor
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
    public ResponseEntity<List<GroupDto>> getGroups(@RequestParam Integer page,
                                                    @RequestParam(required = false) String name) {

        try {
            return ResponseEntity.ok().body(groupService.getGroups(page, name));
        } catch (Exception e) {
            throw new UnexpectedProblemException(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<GroupDto> getGroup(@RequestParam Long id) {

        try {
            return ResponseEntity.ok().body(groupService.getGroup(id));
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

    @PostMapping("/validate-new/group-name")
    public ResponseEntity<Void> validateNewGroupName(@RequestBody String name) {

        if(groupService.isGroupNameTaken(name))
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok().build();
    }
}
