package com.keyword.keywordspring.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.CreateCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.dto.request.IdRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.CommentService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class CommentApiTest {

    @MockBean
    CommentService commentService;

    @MockBean
    JwtUtil jwtUtil;

    MockMvc mockMvc;
    ObjectMapper mapper;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocs,
               WebApplicationContext context) {

        mapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocs))
                .build();
    }

    @Test
    void createComment() throws Exception {

        String request = mapper.writeValueAsString(CreateCommentRequest.builder()
                .content("comment")
                .postId(1L)
                .parentCommentId(1L)
                .build());

        mockMvc.perform(post("/comment/create")
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
    void getComments() throws Exception {
        List<CommentDto> comments = new ArrayList<>();
        comments.add(CommentDto.builder()
                .id(1L)
                .content("comment")
                .user("username")
                .postId(1L)
                .build());

        when(commentService.getComments(any())).thenReturn(comments);

        MvcResult result = mockMvc.perform(get("/comment/get")
                .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(comments));
    }

    @Test
    void editComment() throws Exception {
        String request = mapper.writeValueAsString(EditCommentRequest.builder()
                        .id(1L)
                        .newContent("Edited comment.")
                .build());

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(AppUser.builder().build());

        mockMvc.perform(post("/comment/edit")
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
    void deleteComment() throws Exception {

        String request = mapper.writeValueAsString(IdRequest.builder().id(1L).build());

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(AppUser.builder().id(1L).build());

        mockMvc.perform(post("/comment/delete")
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