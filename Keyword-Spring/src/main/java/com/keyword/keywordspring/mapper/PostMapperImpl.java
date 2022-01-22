package com.keyword.keywordspring.mapper;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.mapper.interf.PostMapper;
import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.Post;
import com.keyword.keywordspring.model.PostVote;
import com.keyword.keywordspring.repository.PostVoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PostMapperImpl implements PostMapper {

    private final PostVoteRepository postVoteRepository;

    @Override
    public PostDto mapToDto(Post post, AppUser user) {

        Optional<PostVote> vote = user == null ?
                Optional.empty() :
                postVoteRepository.findByUserAndPost(user, post);

        return PostDto.builder()
                .id(post.getId())
                .groupName(post.getForumGroup().getGroupName())
                .groupId(post.getForumGroup().getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .dateCreated(post.getDateCreated())
                .username(post.getUser().getUsername())
                .numberOfComments(post.getComments().size())
                .votes(post.getVotes())
                .userVote(vote.isEmpty() ? null : vote.get().getType())
                .build();
    }
}
