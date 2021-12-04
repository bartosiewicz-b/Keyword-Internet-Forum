package com.keyword.keywordspring.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private ForumGroup forumGroup;

    private String title;
    private String description;

    @OneToMany
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private List<Comment> comments;

    private Date dateCreated;
}
