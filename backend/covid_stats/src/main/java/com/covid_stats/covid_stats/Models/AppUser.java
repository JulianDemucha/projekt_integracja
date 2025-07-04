package com.covid_stats.covid_stats.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
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
    @Setter
    private String role = "ROLE_USER";

}

