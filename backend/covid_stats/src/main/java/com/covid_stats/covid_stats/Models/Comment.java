package com.covid_stats.covid_stats.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Column(nullable = false, length = 500) // max 500 znakow
    @Getter @Setter
    private String content;

    @Column(nullable = false)
    @Getter @Setter
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // (x) tym ustalasz klucz obcy (czyli id w appuser)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // żeby autor pola od proxy sie nie serializowaly

    @Getter @Setter
    private AppUser author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference /* dzieki temu pomija to pole i sie nie zapetla serializacja
    (gdyby odpowiedzi mogly miec odpowiedzi, mimo ze tak nie ma na ten moment) */

    @Getter @Setter
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)

    @JsonManagedReference // ma serializowac nie ma pomijac

    @Getter @Setter
    private List<Comment> replies = new ArrayList<>();

    public Comment() {
        this.createdAt = LocalDateTime.now();
    }

    public Comment(String content, AppUser author, Comment parent) {
        this.content = content;
        this.author = author;
        this.parent = parent;
        this.createdAt = LocalDateTime.now();
    }
}