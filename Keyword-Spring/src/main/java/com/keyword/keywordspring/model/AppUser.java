package com.keyword.keywordspring.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @OneToMany
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private List<ForumGroup> ownedGroups;

    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Post> posts;

    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Comment> comments;
}
