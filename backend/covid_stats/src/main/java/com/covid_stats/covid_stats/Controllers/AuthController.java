package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.Models.AppUser;
import com.covid_stats.covid_stats.Repositories.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AppUserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AppUser user) {
        Optional<AppUser> existingUser = userRepo.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            AppUser foundUser = existingUser.get();

            if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
                foundUser.setPassword(null); // ;)
                return ResponseEntity.ok(foundUser);
            } else {
                return ResponseEntity.status(401).body("Nieprawidłowe hasło");
            }
        } else {
            return ResponseEntity.status(404).body("Użytkownik nie istnieje");
        }
    }

}
