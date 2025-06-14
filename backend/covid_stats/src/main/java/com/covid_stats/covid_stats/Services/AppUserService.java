package com.covid_stats.covid_stats.Services;

import com.covid_stats.covid_stats.Models.AppUser;
import com.covid_stats.covid_stats.Repositories.AppUserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserRepo repo;
    private final PasswordEncoder encoder;

    public AppUserService(AppUserRepo repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public AppUser register(AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return repo.save(user);
    }

    public List<AppUser> getAll() {
        return repo.findAll();
    }

    public AppUser upsert(Long id, AppUser user) {
        user.setId(id);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return repo.save(user);
    }

    public AppUser findByUsernameOrThrow(String username) {
        return repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Użytkownik " + username + " nie istnieje"
                ));
    }
}