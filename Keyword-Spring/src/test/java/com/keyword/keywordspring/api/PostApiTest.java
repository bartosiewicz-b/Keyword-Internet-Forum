package com.keyword.keywordspring.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.dto.request.AddPostRequest;
import com.keyword.keywordspring.dto.request.EditPostRequest;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
                .groupId("group")
                .title("title")
                .description("description")
                .username("username")
                .numberOfComments(1)
                .build();
    }

    @Test
    void addPost() throws Exception {
        String request = mapper.writeValueAsString(AddPostRequest.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .groupId(post.getGroupId())
                .build());

        when(postService.add(anyString(), any())).thenReturn(post);

        mockMvc.perform(post("/post/add")
                        .header("Authorization", "token")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(post)))
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getAllPosts() throws Exception {

        List<PostDto> posts = new ArrayList<>();
        posts.add(post);

        when(postService.getAll(anyString(), anyString(), anyInt(), anyString())).thenReturn(posts);

        mockMvc.perform(get("/post/get-all")
                        .param("page", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getPostsCount() throws Exception {

        when(postService.getCount(anyString(), anyString())).thenReturn(1);

        mockMvc.perform(get("/post/get-count"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void getPost() throws Exception {

        when(postService.get(anyString(), anyLong())).thenReturn(post);

        mockMvc.perform(get("/post/get")
                        .param("postId", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void editPost() throws Exception {
        String request = mapper.writeValueAsString(EditPostRequest.builder()
                .title("new title")
                .description("new description")
                .build());

        when(postService.edit(anyString(), any())).thenReturn(post);

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
    void deletePost() throws Exception {

        mockMvc.perform(post("/post/delete")
                        .header("Authorization", "token")
                        .content("1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void upvotePost() throws Exception {

        mockMvc.perform(post("/post/upvote")
                        .header("Authorization", "token")
                        .content("1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    void downvotePost() throws Exception {

        mockMvc.perform(post("/post/downvote")
                        .header("Authorization", "token")
                        .content("1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}