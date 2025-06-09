package com.covid_stats.covid_stats.Services;

import com.covid_stats.covid_stats.Models.AppUser;
import com.covid_stats.covid_stats.Repositories.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
/*
    (x) no to tu masz service dla uzytkownikow cyk pyk pyk (ogolnie wiekszosc metod ktore powinny tu byc
    jak cwel mam w controllerze i kiedys tu je poprzenosze ale mi sie nie chce tera pzdr)
 */
    private final AppUserRepo repo;

    @Autowired
    public DatabaseUserDetailsService(AppUserRepo repo) {
        this.repo = repo;
    }

    //(x) no to normalnie se pobierasz z bazy uzytkownika i to wpierdalasz w obiekt nasz piekny tu w kodzie
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        AppUser user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownik "+username+" nie istnieje"));
        return new User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}

// (x) -> ECommerceStatisticService