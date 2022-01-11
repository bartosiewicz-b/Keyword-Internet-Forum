package com.keyword.keywordspring.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.CreatePostRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;
import com.keyword.keywordspring.dto.request.IdRequest;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.service.interf.JwtUtil;
import com.keyword.keywordspring.service.interf.PostService;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class PostApiTest {

    @MockBean
    PostService postService;
    @MockBean
    JwtUtil jwtUtil;

    MockMvc mockMvc;
    ObjectMapper mapper;

    PostDto post;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocs,
               WebApplicationContext context) {

        mapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(documentationConfiguration(restDocs))
                .build();

        post = PostDto.builder()
                .id(1L)
                .groupName("group")
                .title("title")
                .description("description")
                .username("username")
                .numberOfComments(1)
                .build();
    }

    @Test
    void createPost() throws Exception {
        String request = mapper.writeValueAsString(CreatePostRequest.builder()
                        .title("post title")
                        .description("description")
                        .groupId("1")
                .build());

        mockMvc.perform(post("/post/create")
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
    void getPosts() throws Exception {
        List<PostDto> posts = new ArrayList<>();
        posts.add(post);

        when(postService.getPosts(any(), any(), any(), any())).thenReturn(posts);

        MvcResult result = mockMvc.perform(get("/post/get-all")
                .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(posts));
    }

    @Test
    void getPost() throws Exception {

        when(postService.getPost(anyLong(), any())).thenReturn(post);

        MvcResult result = mockMvc.perform(get("/post/get")
                        .param("id", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(post));
    }

    @Test
    void editPost() throws Exception {
        String request = mapper.writeValueAsString(EditPostRequest.builder()
                        .title("new title")
                        .description("new description")
                .build());

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(AppUser.builder().build());

        mockMvc.perform(post("/post/edit")
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
    void upvotePostApi() throws Exception {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(AppUser.builder().build());

        Map<String, Long> postId = new HashMap<>();
        postId.put("postId", 1L);
        String request = mapper.writeValueAsString(postId);

        mockMvc.perform(post("/post/upvote")
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
    void downvotePostApi() throws Exception {
        when(jwtUtil.getUserFromToken(anyString())).thenReturn(AppUser.builder().build());

        Map<String, Long> postId = new HashMap<>();
        postId.put("postId", 1L);
        String request = mapper.writeValueAsString(postId);

        mockMvc.perform(post("/post/downvote")
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
    void deletePost() throws Exception {

        String request = mapper.writeValueAsString(IdRequest.builder().id(1L).build());

        when(jwtUtil.getUserFromToken(anyString())).thenReturn(AppUser.builder().id(1L).build());

        mockMvc.perform(post("/post/delete")
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