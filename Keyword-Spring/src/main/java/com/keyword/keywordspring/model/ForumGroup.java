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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AppUser owner;

    private String groupName;
    private String description;

    @OneToMany
    @JoinColumn(name = "forum_group_id", referencedColumnName = "id")
    private List<Post> posts;

    private Date dateCreated;
}
