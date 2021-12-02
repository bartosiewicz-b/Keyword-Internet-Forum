package com.keyword.keywordspring.repository;

import com.keyword.keywordspring.model.ForumGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends CrudRepository<ForumGroup, Long> {
    Optional<ForumGroup> findByGroupName(String name);
}
