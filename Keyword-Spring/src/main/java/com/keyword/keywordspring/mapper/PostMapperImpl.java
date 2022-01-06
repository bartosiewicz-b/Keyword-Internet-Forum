package com.keyword.keywordspring.mapper;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.exception.PostDoesNotExistException;
import com.keyword.keywordspring.mapper.interf.PostMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.CommentVote;
import com.keyword.keywordspring.model.Post;
import com.keyword.keywordspring.model.PostVote;
import com.keyword.keywordspring.repository.PostRepository;
import com.keyword.keywordspring.repository.PostVoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PostMapperImpl implements PostMapper {

    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;

    @Override
    public PostDto mapToDto(Post model, AppUser user) {
        Optional<PostVote> vote = null;
        if(user != null)
            vote = postVoteRepository.findByUserAndPost(user, model);

        return PostDto.builder()
                .id(model.getId())
                .groupName(model.getForumGroup().getGroupName())
                .groupId(model.getForumGroup().getId())
                .title(model.getTitle())
                .description(model.getDescription())
                .dateCreated(model.getDateCreated())
                .username(model.getUser().getUsername())
                .numberOfComments(model.getComments().size())
                .votes(model.getVotes())
                .userVote(null == vote || vote.isEmpty() ? null : vote.get().getType())
                .build();
    }
}
