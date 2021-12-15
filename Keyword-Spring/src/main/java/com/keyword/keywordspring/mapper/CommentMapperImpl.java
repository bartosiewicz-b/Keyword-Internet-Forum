package com.keyword.keywordspring.mapper;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.exception.CommentDoesNotExistException;
import com.keyword.keywordspring.mapper.interf.CommentMapper;
import com.keyword.keywordspring.model.Comment;
import com.keyword.keywordspring.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class CommentMapperImpl implements CommentMapper {

    private final CommentRepository commentRepository;

    @Override
    public CommentDto mapToDto(Comment model) {
        return CommentDto.builder()
                .id(model.getId())
                .content(model.getContent())
                .parentCommentId(null == model.getParentComment() ?
                        null :
                        model.getParentComment().getId())
                .user(model.getUser().getUsername())
                .postId(model.getPost().getId())
                .dateCreated(model.getDateCreated())
                .build();
    }

    @Override
    public Comment mapToModel(CommentDto dto) {

        Optional<Comment> comment = commentRepository.findById(dto.getId());

        if(comment.isPresent())
            return comment.get();

        throw new CommentDoesNotExistException(dto.getId());
    }
}
