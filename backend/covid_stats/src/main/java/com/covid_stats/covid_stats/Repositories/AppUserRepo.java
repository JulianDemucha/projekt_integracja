package com.covid_stats.covid_stats.Repositories;
import com.covid_stats.covid_stats.Models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
