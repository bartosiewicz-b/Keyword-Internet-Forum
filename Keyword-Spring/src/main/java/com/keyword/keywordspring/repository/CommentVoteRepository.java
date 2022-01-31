package com.keyword.keywordspring.repository;

import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.Comment;
import com.keyword.keywordspring.model.CommentVote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentVoteRepository extends CrudRepository<CommentVote, Long> {
    Optional<CommentVote> findByUserAndComment(AppUser user, Comment comment);

    void deleteAllByComment(Comment comment);
}
