package com.keyword.keywordspring.repository;

import com.keyword.keywordspring.model.ForumGroup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends CrudRepository<ForumGroup, String> {

    Optional<ForumGroup> findByGroupName(String name);

    List<ForumGroup> findAll(Pageable pageable);
    List<ForumGroup> findByGroupNameLike(String groupName, Pageable pageable);
}
