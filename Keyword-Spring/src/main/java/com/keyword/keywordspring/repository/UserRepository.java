package com.keyword.keywordspring.repository;

import com.keyword.keywordspring.model.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<AppUser, Long> {

    public Optional<AppUser> findByUsername(String username);
    public Optional<AppUser> findByEmail(String email);
}
