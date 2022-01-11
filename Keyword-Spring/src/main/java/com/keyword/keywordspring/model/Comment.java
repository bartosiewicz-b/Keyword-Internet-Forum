package com.keyword.keywordspring.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private Post post;

    @OneToOne
    private Comment parentComment;

    private Date dateCreated;

    private Boolean edited;

    private Integer votes;
}
