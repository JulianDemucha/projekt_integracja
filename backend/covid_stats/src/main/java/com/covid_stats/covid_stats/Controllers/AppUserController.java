package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.Models.AppUser;
import com.covid_stats.covid_stats.Repositories.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/users")
public class AppUserController {

    private final AppUserRepo repo;

    private final PasswordEncoder encoder;

    public AppUserController(AppUserRepo repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    //rejestracja
    @PostMapping
    public AppUser createUser(@RequestBody AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return repo.save(user);
    }

    //get na wszystkich uzytkownikow
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AppUser> listUsers() {
        return repo.findAll();
    }

    // edycja/create uzytkownika
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public AppUser upsertUser(
            @PathVariable Long id,
            @RequestBody AppUser user
    ) {
        user.setId(id);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return repo.save(user);
    }
}
