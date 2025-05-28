package com.covid_stats.covid_stats;

import com.covid_stats.covid_stats.Models.AppUser;
import com.covid_stats.covid_stats.Repositories.AppUserRepo;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CovidStatsApplication {


	@Bean
	CommandLineRunner initUser(AppUserRepo repo, PasswordEncoder encoder) {
		return args -> {
			if (repo.findByUsername("user").isEmpty()) {
				AppUser u = new AppUser();
				u.setUsername("user");
				u.setPassword(encoder.encode("password"));
				repo.save(u);
				System.out.println(">>> Utworzono użytkownika 'user' z hasłem 'password'");
			}
		};
	}

	public static void main(String[] args) {SpringApplication.run(CovidStatsApplication.class, args);}
}
