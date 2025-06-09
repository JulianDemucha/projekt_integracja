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
@EnableMethodSecurity
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
                        // wszystkie GETY komentarzy na public
                        .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/stats", "/api/stats2", "/api/stats3").permitAll()

                        // logowanie
                        .requestMatchers("/auth/**").permitAll()

                        /* POST do rejestracji (niestety nie ma zadnej capathy ani weryfikacji maila,
                        ale moze ktos nie z ddosuje) */
                        .requestMatchers(HttpMethod.POST, "/users/**").permitAll()

                        // GET,PUT na users dla adm w razie co
                        .requestMatchers("/users/**").hasRole("ADMIN")

                        // POST na komentarze (tworzenie dla zalogowanych)
                        .requestMatchers(HttpMethod.POST, "/api/comments/**").authenticated()
                        // DELETE na komentarze (niby dla zalogowanych ale w kontrolerze jest preauthorize)
                        // "hasRole('ADMIN') or @commentSecurity.isCommentAuthor(#id, authentication.name)"
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/**").authenticated()
                        // GETY na export dla zalogowanych
                        .requestMatchers(HttpMethod.GET, "/api/stats/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/stats2/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/stats3/**").authenticated()

                        // wszystkie pozostałe requesty – dla roli admin
                        .anyRequest().hasRole("ADMIN")
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // dokladny adres frontendu
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        // zezwolone metody
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // zezwolone naglowki
        config.setAllowedHeaders(List.of("*"));
        // wysyłanie credentiali (np. ciasteczek lub naglowka Authorization)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // CORS dla endpointów
        source.registerCorsConfiguration("/api/**", config);
        source.registerCorsConfiguration("/auth/**", config);
        source.registerCorsConfiguration("/users/**", config);
        source.registerCorsConfiguration("/stats3/export/**", config);

        return source;
    }
}
