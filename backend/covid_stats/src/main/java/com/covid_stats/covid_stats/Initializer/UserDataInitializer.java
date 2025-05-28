package com.covid_stats.covid_stats.Initializer;

import com.covid_stats.covid_stats.Models.AppUser;
import com.covid_stats.covid_stats.Repositories.AppUserRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final AppUserRepo repo;
    private final PasswordEncoder encoder;

    private boolean initialized = false;

    public UserDataInitializer(AppUserRepo repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (initialized) return;
        if (repo.findByUsername("user").isEmpty()) {
            AppUser u = new AppUser();
            u.setUsername("user");
            u.setPassword(encoder.encode("password"));
            repo.save(u);
        }
        initialized = true;
    }
}
