package com.keyword.keywordspring.repository;

import com.keyword.keywordspring.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostVoteRepository extends CrudRepository<PostVote, Long> {
    Optional<PostVote> findByUserAndPost(AppUser user, Post post);

    void deleteAllByPost(Post post);
}
