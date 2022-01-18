package com.keyword.keywordspring.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    private Date dateCreated;
    private int nrOfComments;
    private int nrOfPosts;

    @OneToMany
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private List<ForumGroup> ownedGroups;

    @ManyToMany
    private List<ForumGroup> moderatedGroups;

    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Post> posts;

    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Comment> comments;

    @ManyToMany
    private List<ForumGroup> subscribed;
}
