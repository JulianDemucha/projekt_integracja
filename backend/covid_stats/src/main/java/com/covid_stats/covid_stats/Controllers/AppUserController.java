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

    @Autowired
    private AppUserRepo repo;

    @Autowired
    private PasswordEncoder encoder;

    /**
     * 1) Rejestracja nowego użytkownika – dostępne publicznie.
     */
    @PostMapping
    public AppUser createUser(@RequestBody AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return repo.save(user);
    }

    /**
     * 2) Pobierz wszystkich użytkowników – tylko ADMIN.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AppUser> listUsers() {
        return repo.findAll();
    }

    /**
     * 3) Edytuj lub utwórz użytkownika o danym ID – tylko ADMIN.
     */
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
