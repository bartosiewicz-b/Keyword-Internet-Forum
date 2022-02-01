package com.keyword.keywordspring.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    private String avatarUrl;

    @OneToMany
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private List<ForumGroup> ownedGroups;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "moderators", cascade = CascadeType.ALL)
    private List<ForumGroup> moderatedGroups;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subscribers", cascade = CascadeType.ALL)
    private List<ForumGroup> subscribedGroups;

    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Post> posts;

    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if(Objects.isNull(o)) return false;
        if(!(o instanceof AppUser)) return false;

        return id.equals(((AppUser) o).id);
    }
}
