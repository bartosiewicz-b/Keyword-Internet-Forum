package com.keyword.keywordspring.repository;

import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    List<Post> findAll(Pageable pageable);
    List<Post> findByTitleLike(String title, Pageable pageable);
    List<Post> findByForumGroupLike(ForumGroup group, Pageable pageable);
    List<Post> findByTitleLikeAndForumGroupLike(String title, ForumGroup group, Pageable pageable);
}
