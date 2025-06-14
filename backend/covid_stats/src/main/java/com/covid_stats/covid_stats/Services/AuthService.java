package com.covid_stats.covid_stats.Services;

import com.covid_stats.covid_stats.Models.AppUser;
import com.covid_stats.covid_stats.Repositories.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AppUserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> login(AppUser user) {
        Optional<AppUser> existingUser = userRepo.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            AppUser foundUser = existingUser.get();

            if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
                foundUser.setPassword(null);        // ;)
                return ResponseEntity.ok(foundUser);
            } else {
                return ResponseEntity.status(401).body("Nieprawidłowe hasło");
            }
        } else {
            return ResponseEntity.status(404).body("Użytkownik nie istnieje");
        }
    }
}