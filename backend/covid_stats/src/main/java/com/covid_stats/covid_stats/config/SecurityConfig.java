package com.covid_stats.covid_stats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity  // Włączamy możliwość użycia @PreAuthorize itp.
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1) Wszystkie GETy do komentarzy są publiczne
                        .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/stats", "/api/stats2", "/api/stats3").permitAll()
                        // 2) Logowanie i /auth/** są publiczne
                        .requestMatchers("/auth/**").permitAll()
                        // 3) Rejestracja nowego użytkownika (POST /users) jest publiczna
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        // 4) Pozostałe operacje na /users/** (GET, PUT itd.) – tylko ADMIN
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        // 5) Tworzenie lub odpowiadanie na komentarz – każdy zalogowany
                        .requestMatchers(HttpMethod.POST, "/api/comments/**").authenticated()
                        // 6) Usuwanie komentarzy – każdy zalogowany, ale dopiero @PreAuthorize w kontrolerze sprawdzi autorstwo/ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/**").authenticated()
                        // 7) Wszystkie pozostałe requesty – wymagają uwierzytelnienia
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // dokładny adres frontendu
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        // zezwolone metody
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // zezwolone nagłówki
        config.setAllowedHeaders(List.of("*"));
        // zezwalamy na wysyłanie credentiali (np. ciasteczek lub nagłówka Authorization)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // dopuszczamy CORS dla endpointów komentujących, auth, użytkownicy itp.:
        source.registerCorsConfiguration("/api/**", config);
        source.registerCorsConfiguration("/auth/**", config);
        source.registerCorsConfiguration("/users/**", config);

        return source;
    }
}
