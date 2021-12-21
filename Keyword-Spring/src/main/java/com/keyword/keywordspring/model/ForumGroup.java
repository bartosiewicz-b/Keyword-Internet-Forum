package com.keyword.keywordspring.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForumGroup {

    @Id
    private String id;

    @ManyToOne
    private AppUser owner;

    private String groupName;

    @Lob
    private String description;

    @OneToMany
    @JoinColumn(name = "forum_group_id", referencedColumnName = "id")
    private List<Post> posts;

    private Date dateCreated;

    private Integer subscriptions;
}
