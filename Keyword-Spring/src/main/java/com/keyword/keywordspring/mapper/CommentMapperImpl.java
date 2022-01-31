package com.keyword.keywordspring.mapper;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.mapper.interf.CommentMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.Comment;
import com.keyword.keywordspring.model.CommentVote;
import com.keyword.keywordspring.repository.CommentVoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class CommentMapperImpl implements CommentMapper {

    private final CommentVoteRepository commentVoteRepository;

    @Override
    public CommentDto mapToDto(Comment comment, AppUser user) {

        CommentVote vote = commentVoteRepository.findByUserAndComment(user, comment).orElse(null);

        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .parentCommentId(Objects.isNull(comment.getParentComment()) ?
                        null :
                        comment.getParentComment().getId())
                .user(comment.getUser().getUsername())
                .userAvatarUrl(comment.getUser().getAvatarUrl())
                .postId(comment.getPost().getId())
                .dateCreated(comment.getDateCreated())
                .votes(comment.getVotes())
                .userVote(Objects.isNull(vote) ?
                        null :
                        vote.getType())
                .build();
    }
}
