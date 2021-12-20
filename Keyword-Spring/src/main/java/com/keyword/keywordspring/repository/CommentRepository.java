package com.keyword.keywordspring.repository;

import com.keyword.keywordspring.model.Comment;
import com.keyword.keywordspring.model.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findAllByPostOrderByVotesDesc(Post post);
}
