package com.covid_stats.covid_stats.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(unique = true, nullable = false)
    private String username;

    @Setter
    @Getter
    @Column(nullable = false)
    private String password;

    @Getter
    private String role = "ROLE_USER";


}

