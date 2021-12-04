package com.keyword.keywordspring.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @NotNull
    private Long id;

    @NotNull
    private String content;

    private Long parentCommentId;

    @NotNull
    private String user;

    @NotNull
    private Long postId;

    @NotNull
    private Date dateCreated;
}
