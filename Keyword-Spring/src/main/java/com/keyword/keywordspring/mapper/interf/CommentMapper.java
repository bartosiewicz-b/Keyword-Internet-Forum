package com.keyword.keywordspring.mapper.interf;

import com.keyword.keywordspring.dto.model.CommentDto;
import com.keyword.keywordspring.model.Comment;

public interface CommentMapper {

    CommentDto mapToDto(Comment model);
    Comment mapToModel(CommentDto dto);
}
