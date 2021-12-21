package com.keyword.keywordspring.dto.model;

import com.keyword.keywordspring.model.VoteType;
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
public class PostDto {

    @NotNull
    private Long id;

    @NotNull
    private String groupName;

    @NotNull
    private String groupId;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Date dateCreated;

    @NotNull
    private String username;

    @NotNull
    private Integer numberOfComments;

    @NotNull
    private Integer votes;

    private VoteType userVote;
}
