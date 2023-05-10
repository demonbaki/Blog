package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.Creation;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Comment extends Creation {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false) // non puo' essere nulla  == NOT NULL di sql
    private String comment;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="post_id", nullable = false) // not null
    private Post post;


    private boolean censored = false;

    public Comment(String comment, User author, Post post) {
        this.comment = comment;
        this.author = author;
        this.post = post;

    }
}
