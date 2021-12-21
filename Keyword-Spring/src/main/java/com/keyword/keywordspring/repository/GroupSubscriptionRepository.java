package com.keyword.keywordspring.repository;

import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.model.GroupSubscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupSubscriptionRepository extends CrudRepository<GroupSubscription, Long> {

    Optional<GroupSubscription> findByUserAndGroup(AppUser user, ForumGroup group);
}
