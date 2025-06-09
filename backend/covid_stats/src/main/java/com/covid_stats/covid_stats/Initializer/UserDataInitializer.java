package com.covid_stats.covid_stats.Initializer;

import com.covid_stats.covid_stats.Models.AppUser;
import com.covid_stats.covid_stats.Repositories.AppUserRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

    /*
      ! tworzy roota jezeli jeszcze go nie ma !
      cala klasa zamiast @EvventListener w configu, bo nie ma
      configu ogolnego tylko nazwany stricte "Security Config"
     */

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
        if (repo.findByUsername("root").isEmpty()) {
            AppUser u = new AppUser();
            u.setUsername("root");
            u.setPassword(encoder.encode("root"));
            u.setRole("ROLE_ADMIN");
            repo.save(u);
        }
        initialized = true;
    }
}
