package com.keyword.keywordspring.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.dto.request.*;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.GroupService;
import com.keyword.keywordspring.service.interf.JwtUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @MockBean
    JwtUtil jwtUtil;

    MockMvc mockMvc;
    ObjectMapper mapper;

    GroupDto group;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocs,
               WebApplicationContext context) {

        mapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocs))
                .build();

        group = GroupDto.builder()
                .id("1")
                .groupName("group")
                .description("description")
                .build();
    }

    @Test
    void createGroup() throws Exception {
        String request = mapper.writeValueAsString(CreateGroupRequest.builder()
                .groupName("group")
                .description("description")
                .build());

        mockMvc.perform(post("/group/create")
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
    void getGroups() throws Exception {
        List<GroupDto> groups = new ArrayList<>();
        groups.add(group);

        when(groupService.getGroups(any(), any(), any())).thenReturn(groups);

        MvcResult result = mockMvc.perform(get("/group/get-all")
                .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(groups));
    }

    @Test
    void getGroupsCount() throws Exception {
        when(groupService.getGroupsCount(anyString())).thenReturn(0);

        MvcResult result = mockMvc.perform(get("/group/get-all-count"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), "0");
    }

    @Test
    void getGroup() throws Exception {

        when(groupService.getGroup(anyString(), any())).thenReturn(group);

        MvcResult result = mockMvc.perform(get("/group/get")
                        .param("id", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(group));
    }

    @Test
    void editGroup() throws Exception {
        String request = mapper.writeValueAsString(EditGroupRequest.builder()
                .id("1")
                .groupName("new group name")
                .description("new description")
                .build());

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(AppUser.builder().build());

        mockMvc.perform(post("/group/edit")
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
        Map<String, String> map = new HashMap<>();
        map.put("groupName", "new group name");
        String request = mapper.writeValueAsString(map);

        when(groupService.isGroupNameTaken(anyString())).thenReturn(false);

        mockMvc.perform(post("/group/validate-new/group-name")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void subscribeGroup() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("groupId", "group");
        String request = mapper.writeValueAsString(map);

        mockMvc.perform(post("/group/subscribe")
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
    void getSubscribedGroups() throws Exception {

        mockMvc.perform(get("/group/get-subscribed")
                        .header("Authorization", "token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void transferOwnership() throws Exception {

        String request = mapper.writeValueAsString(
                SubscriberRequest.builder()
                        .username("username")
                        .groupId("groupId")
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
    void addModerator() throws Exception {

        String request = mapper.writeValueAsString(
                SubscriberRequest.builder()
                        .username("username")
                        .groupId("groupId")
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
    void deleteModerator() throws Exception {
        String request = mapper.writeValueAsString(
                SubscriberRequest.builder()
                        .username("username")
                        .groupId("groupId")
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
    void getSubscribers() throws Exception {
        List<UserDto> users = new ArrayList<>();
        users.add(UserDto.builder().username("username").build());

        when(groupService.getSubscribers(anyString(), anyString())).thenReturn(users);

        MvcResult result = mockMvc.perform(get("/group/get-subscribers")
                        .param("username", "username")
                        .param("groupId", "groupId")
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
    void getModerators() throws Exception {
        List<UserDto> users = new ArrayList<>();
        users.add(UserDto.builder().username("username").build());

        when(groupService.getModerators(anyString())).thenReturn(users);

        MvcResult result = mockMvc.perform(get("/group/get-moderators")
                        .header("Authorization", "token")
                        .param("groupId", "groupId")
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
    void isModerator() throws Exception {
        String request = mapper.writeValueAsString(
                SubscriberRequest.builder()
                        .username("username")
                        .groupId("groupId")
                        .build());

        when(groupService.isModerator(anyString(), anyString())).thenReturn(true);

        MvcResult result = mockMvc.perform(get("/group/is-moderator")
                        .header("Authorization", "token")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), "true");
    }
}