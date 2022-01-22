package com.keyword.keywordspring.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.dto.request.AddCommentRequest;
import com.keyword.keywordspring.dto.request.EditCommentRequest;
import com.keyword.keywordspring.service.interf.CommentService;
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

    MockMvc mockMvc;
    ObjectMapper mapper;

    CommentDto commentDto;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocs,
               WebApplicationContext context) {

        mapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocs))
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .postId(1L)
                .user("username")
                .content("content")
                .parentCommentId(2L)
                .build();
    }

    @Test
    void addComment() throws Exception {

        String request = mapper.writeValueAsString(AddCommentRequest.builder()
                .content(commentDto.getContent())
                .postId(commentDto.getPostId())
                .parentCommentId(commentDto.getParentCommentId())
                .build());

        when(commentService.add(anyString(), any())).thenReturn(commentDto);

        MvcResult res = mockMvc.perform(post("/comment/add")
                        .header("Authorization", "token")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(mapper.writeValueAsString(commentDto), res.getResponse().getContentAsString());
    }

    @Test
    void getAllComments() throws Exception {

        String request = mapper.writeValueAsString(commentDto.getId());

        List<CommentDto> comments = new ArrayList<>();
        comments.add(commentDto);

        when(commentService.getAll(anyString(), anyLong())).thenReturn(comments);

        mockMvc.perform(get("/comment/get-all")
                .param("postId", request))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();
    }

    @Test
    void editComment() throws Exception {
        String request = mapper.writeValueAsString(EditCommentRequest.builder()
                        .id(commentDto.getId())
                        .newContent("edited content")
                .build());

        when(commentService.edit(anyString(), any())).thenReturn(commentDto);

        MvcResult res = mockMvc.perform(post("/comment/edit")
                .header("Authorization", "token")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(mapper.writeValueAsString(commentDto), res.getResponse().getContentAsString());
    }

    @Test
    void deleteComment() throws Exception {

        String request = mapper.writeValueAsString(commentDto.getId());

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

    @Test
    void upvoteComment() throws Exception {

        String request = mapper.writeValueAsString(commentDto.getId());

        mockMvc.perform(post("/comment/upvote")
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
    void downvoteComment() throws Exception {

        String request = mapper.writeValueAsString(commentDto.getId());

        mockMvc.perform(post("/comment/downvote")
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