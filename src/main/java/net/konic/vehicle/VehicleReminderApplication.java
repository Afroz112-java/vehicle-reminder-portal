package net.konic.vehicle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// Enable caching and scheduling features

@EnableCaching
@EnableScheduling
public class VehicleReminderApplication {

	// Main method to run the Spring Boot application
	public static void main(String[] args) {
		SpringApplication.run(VehicleReminderApplication.class, args);
	}

}
