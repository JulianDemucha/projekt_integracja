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
@EnableMethodSecurity  // @PreAuthorize itp.
public class SecurityConfig {

    // (x) to takie gowno do hashowania hasel ze springsecurity
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* (x) no to tu duzo jest jakiegos pierdu pierdu i rozprawke na temat kazdego by trzeba bylo pisac
    a sam czesci niezbyt ogarniam wiec no pomine kapke.

    ogolnie w tym filterchainie se dajesz jakies konfiguracje, permity na dostep do konkretnych endpointow
    (na endpointach masz jsony z danymi lub "wejscia" do danych na ktore sie wysyla tez dane zeby je odebrac
    i to wszystko kmini rest)

    a to authenticated masz w AuthControllerze
     */
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

                        // wszystkie pozostałe requesty – dla roli admin
                        .anyRequest().hasRole("ADMIN")
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /* (x) cors to takie gowno co ci chroni zeby ktos nie zapierdolil danych w pizdu jakichs fajnych
    albo nie wpierdalal miliona danych tak o bo moze cn

    no chroni endpointy po prostu

    a tam nizej se zezwalamy dla fronta na wszystko bo jest sigmastyczny (nie ogarniam i tak czasami jak to dziala
    bo i tak musze permittowac dziwke)

    nie no ogolnie ten filterchain dziala do wszystkiego a ten cors jest jakby do innych witryn js czyli np
    jak w filterchainie masz spermittowane cos, ale w tym corsie obejmujesz ten endpoint corsem ale tez
    masz spermittowanego naszego localhosta na portcie 5173 wiec jak javascript z tej naszej strony wysle
    requesta do servera to go wpusci ale np jak se w przegladarce wpiszesz dany endpoint to cie nie wpusci cn
     */
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

        return source;
    }
}

// (x) -> ../Controllers/appUserController