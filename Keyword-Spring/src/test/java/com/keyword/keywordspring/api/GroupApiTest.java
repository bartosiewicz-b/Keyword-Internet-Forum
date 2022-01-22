package com.keyword.keywordspring.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.dto.request.*;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class GroupApiTest {

    @MockBean
    GroupService groupService;

    MockMvc mockMvc;
    ObjectMapper mapper;

    GroupDto group;

    AppUser user;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocs,
               WebApplicationContext context) {

        mapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocs))
                .build();

        group = GroupDto.builder()
                .id("group")
                .groupName("group")
                .description("description")
                .build();

        user = AppUser.builder()
                .id(1L)
                .username("username")
                .build();
    }

    @Test
    void addGroup() throws Exception {
        String request = mapper.writeValueAsString(AddGroupRequest.builder()
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .build());

        when(groupService.add(anyString(), any())).thenReturn(group);

        MvcResult res = mockMvc.perform(post("/group/add")
                .header("Authorization", "token")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(mapper.writeValueAsString(group), res.getResponse().getContentAsString());
    }

    @Test
    void getAllGroups() throws Exception {

        List<GroupDto> groups = new ArrayList<>();
        groups.add(group);

        when(groupService.getAll(anyString(), any(), anyString())).thenReturn(groups);

        mockMvc.perform(get("/group/get-all")
                .param("page", "0")
                .param("keyword", "group"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getGroupsCount() throws Exception {
        when(groupService.getCount(group.getGroupName())).thenReturn(1);

        MvcResult result = mockMvc.perform(get("/group/get-count")
                .param("keyword", group.getGroupName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals("1", result.getResponse().getContentAsString());
    }

    @Test
    void getGroup() throws Exception {

        when(groupService.get(anyString(), anyString())).thenReturn(group);

        mockMvc.perform(get("/group/get")
                .param("groupId", group.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void editGroup() throws Exception {
        EditGroupRequest request = EditGroupRequest.builder()
                .id(group.getId())
                .groupName("new group name")
                .description("new description")
                .build();

        GroupDto edited = GroupDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .groupName(request.getGroupName())
                .build();

        when(groupService.edit(anyString(), any())).thenReturn(edited);

        MvcResult res = mockMvc.perform(post("/group/edit")
                        .header("Authorization", "token")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(mapper.writeValueAsString(edited), res.getResponse().getContentAsString());
    }

    @Test
    void deleteGroup() throws Exception {
        mockMvc.perform(post("/group/delete")
                        .header("Authorization", "token")
                        .content(mapper.writeValueAsString(group.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void transferGroupOwnership() throws Exception {

        String request = mapper.writeValueAsString(
                GroupUserRequest.builder()
                        .username(user.getUsername())
                        .groupId(group.getId())
                        .build());

        mockMvc.perform(post("/group/transfer-ownership")
                        .header("Authorization", "token")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getSubscribersFromGroup() throws Exception {

        List<UserDto> users = new ArrayList<>();
        users.add(UserDto.builder().username(user.getUsername()).build());

        when(groupService.getSubscribers(group.getId(), user.getUsername())).thenReturn(users);

        MvcResult result = mockMvc.perform(get("/group/get-subscribers")
                        .param("groupId", group.getId())
                        .param("keyword", user.getUsername())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(users));
    }

    @Test
    void subscribeGroup() throws Exception {
        mockMvc.perform(post("/group/subscribe")
                        .header("Authorization", "token")
                        .content(group.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getModeratorsFromGroup() throws Exception {
        List<UserDto> users = new ArrayList<>();
        users.add(UserDto.builder().username(user.getUsername()).build());

        when(groupService.getModerators(group.getId())).thenReturn(users);

        MvcResult result = mockMvc.perform(get("/group/get-moderators")
                        .header("Authorization", "token")
                        .param("groupId", group.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(mapper.writeValueAsString(users), result.getResponse().getContentAsString());
    }

    @Test
    void isGroupModerator() throws Exception {
        String request = mapper.writeValueAsString(
                GroupUserRequest.builder()
                        .username(user.getUsername())
                        .groupId(group.getId())
                        .build());

        when(groupService.isModerator("username", group.getId())).thenReturn(true);

        MvcResult res = mockMvc.perform(get("/group/is-moderator")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals("true", res.getResponse().getContentAsString());
    }

    @Test
    void addGroupModerator() throws Exception {

        String request = mapper.writeValueAsString(
                GroupUserRequest.builder()
                        .username(user.getUsername())
                        .groupId(group.getId())
                        .build());

        mockMvc.perform(post("/group/add-moderator")
                        .header("Authorization", "token")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void deleteGroupModerator() throws Exception {
        String request = mapper.writeValueAsString(
                GroupUserRequest.builder()
                        .username(user.getUsername())
                        .groupId(group.getId())
                        .build());

        mockMvc.perform(post("/group/delete-moderator")
                        .header("Authorization", "token")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void validateNewGroupName() throws Exception {

        when(groupService.isGroupNameTaken(group.getGroupName())).thenReturn(false);

        mockMvc.perform(post("/group/validate-new-group-name")
                .content(group.getGroupName())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}