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
public class ForumGroup implements Serializable {

    @Id
    private String id;

    @ManyToOne
    private AppUser owner;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "group_moderators",
        joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<AppUser> moderators;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "group_subscribers",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<AppUser> subscribers;

    private String groupName;

    @Lob
    private String description;

    private String avatarUrl;

    @OneToMany
    @JoinColumn(name = "forum_group_id", referencedColumnName = "id")
    private List<Post> posts;

    private Date dateCreated;

    private Integer subscriptions;
}
