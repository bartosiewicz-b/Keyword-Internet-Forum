package com.keyword.keywordspring.mapper;

import com.keyword.keywordspring.dto.model.UserDto;
import com.keyword.keywordspring.model.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperImplTest {

    @InjectMocks
    UserMapperImpl userMapper;

    AppUser user;

    @BeforeEach
    void setUp() {
        user = AppUser.builder()
                .username("username")
                .dateCreated(new Date())
                .nrOfComments(5)
                .nrOfPosts(6)
                .build();
    }

    @Test
    void mapToDto() {

        UserDto res = userMapper.mapToDto(user);

        assertNotNull(res);
        assertEquals(user.getUsername(), res.getUsername());
        assertEquals(user.getDateCreated(), res.getDateCreated());
        assertEquals(user.getNrOfComments(), res.getComments());
        assertEquals(user.getNrOfPosts(), res.getPosts());
    }
}