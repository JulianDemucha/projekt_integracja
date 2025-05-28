package com.covid_stats.covid_stats;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CovidStatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovidStatsApplication.class, args);
	}

// dodawanie usera roota przy starcie jak na nowej maszynie
//	@Bean
//	CommandLineRunner initUser(AppUserRepo repo, PasswordEncoder encoder) {
//		return args -> {
//			if (repo.findByUsername("root").isEmpty()) {
//				AppUser u = new AppUser();
//				u.setUsername("root");
//				u.setPassword(encoder.encode("root"));
//				repo.save(u);
//				System.out.println(">>> Utworzono użytkownika 'root' z hasłem 'root'");
//			}
//		};
//	}

}
