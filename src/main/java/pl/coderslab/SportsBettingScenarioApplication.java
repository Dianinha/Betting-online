package pl.coderslab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SportsBettingScenarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportsBettingScenarioApplication.class, args);
	}
}
