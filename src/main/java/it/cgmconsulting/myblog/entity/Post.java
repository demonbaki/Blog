package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.Creation;
import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter@Setter@NoArgsConstructor@EqualsAndHashCode
public class Post extends CreationUpdate {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,length = 100,unique = true)
    private String title;

    @Column(nullable = false, length = 3000)
    private String content;

    @Column(nullable = false)
    private String overview;

    private String image;//contiene il nome e l'estensione del file di tipo immagine(pippo.png)
    private boolean published = false;

    @ManyToOne	@ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="post_categories",
            joinColumns = {@JoinColumn(name="post_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="category_name", referencedColumnName = "categoryName")})
    private Set<Category> categories = new HashSet<>();


    @OneToMany(mappedBy = "post",orphanRemoval = true,cascade = CascadeType.ALL)
    List<Comment>comments=new ArrayList<>();

    public Post(String title, String content, String overview, User author) {
        this.title = title;
        this.content = content;
        this.overview = overview;
        this.author = author;
    }


    public Post(long id) {
        this.id = id;
    }
}