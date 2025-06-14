package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.Models.AppUser;
import com.covid_stats.covid_stats.Services.AppUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService service;

    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @PostMapping
    public AppUser createUser(@RequestBody AppUser user) {
        return service.register(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AppUser> listUsers() {
        return service.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public AppUser upsertUser(
            @PathVariable Long id,
            @RequestBody AppUser user
    ) {
        return service.upsert(id, user);
    }
}