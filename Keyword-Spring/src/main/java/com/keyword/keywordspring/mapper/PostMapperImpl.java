package com.keyword.keywordspring.mapper;

import com.keyword.keywordspring.dto.model.PostDto;
import com.keyword.keywordspring.exception.PostDoesNotExistException;
import com.keyword.keywordspring.mapper.interf.PostMapper;
import com.keyword.keywordspring.model.Post;
import com.keyword.keywordspring.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PostMapperImpl implements PostMapper {

    private final PostRepository postRepository;

    @Override
    public PostDto mapToDto(Post model) {
        return PostDto.builder()
                .id(model.getId())
                .groupName(model.getForumGroup().getGroupName())
                .title(model.getTitle())
                .description(model.getDescription())
                .dateCreated(model.getDateCreated())
                .username(model.getUser().getUsername())
                .numberOfComments(model.getComments().size())
                .build();
    }

    @Override
    public Post mapToModel(PostDto dto) {
        Optional<Post> post = postRepository.findById(dto.getId());

        if(post.isPresent())
            return post.get();

        throw new PostDoesNotExistException(dto.getId());
    }
}
