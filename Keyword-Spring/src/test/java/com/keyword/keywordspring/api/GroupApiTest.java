package com.keyword.keywordspring.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyword.keywordspring.dto.model.GroupDto;
import com.keyword.keywordspring.dto.request.CreateGroupRequest;
import com.keyword.keywordspring.dto.request.EditGroupRequest;
import com.keyword.keywordspring.dto.request.IdStringRequest;
import com.keyword.keywordspring.dto.request.NameRequest;
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
        String request = mapper.writeValueAsString(NameRequest.builder().name("new group name").build());

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
        String request = mapper.writeValueAsString(IdStringRequest.builder().id("group").build());

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
    void deleteGroup() throws Exception {
        String request = mapper.writeValueAsString(IdStringRequest.builder().id("group").build());

        mockMvc.perform(post("/group/delete")
                        .header("Authorization", "token")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}