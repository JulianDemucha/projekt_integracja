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

    @Column(nullable = false, length = 1000)
    @Getter @Setter
    private String content;

    @Column(nullable = false)
    @Getter @Setter
    private LocalDateTime createdAt;

    // Zamiast @JsonBackReference, dajemy @JsonIgnoreProperties, żeby autor się serializował
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Getter @Setter
    private AppUser author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnoreProperties({"replies"})
    @JsonBackReference
    @Getter @Setter
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonBackReference
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
