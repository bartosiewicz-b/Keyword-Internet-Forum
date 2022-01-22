package com.keyword.keywordspring.exception;

import com.keyword.keywordspring.model.AppUser;
import com.keyword.keywordspring.model.Comment;
import com.keyword.keywordspring.model.ForumGroup;
import com.keyword.keywordspring.model.Post;
import org.springframework.security.access.AccessDeniedException;

public class UnauthorizedException extends AccessDeniedException {

    public UnauthorizedException() {
        super("Unauthorized");
    }

    public UnauthorizedException(String login) {
        super("Unauthorized login: " + login);
    }

    public UnauthorizedException(AppUser user, Comment comment) {
        super("Unauthorized access to comment:" + comment.getId() + ", by user: " + user.getUsername());
    }

    public UnauthorizedException(AppUser user, ForumGroup group) {
        super("Unauthorized access to group:" + group.getId() + ", by user: " + user.getUsername());
    }

    public UnauthorizedException(AppUser user, Post post) {
        super("Unauthorized access to post:" + post.getId() + ", by user: " + user.getUsername());
    }
}