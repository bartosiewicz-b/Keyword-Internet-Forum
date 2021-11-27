package com.keyword.keywordspring.repository;

import com.keyword.keywordspring.model.InvalidToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvalidTokenRepository extends CrudRepository<InvalidToken, Long> {
    Optional<InvalidToken> findByToken(String token);
}
