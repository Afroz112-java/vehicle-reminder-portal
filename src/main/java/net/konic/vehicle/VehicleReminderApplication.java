package net.konic.vehicle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching            // Turns on caching so your app can store frequently used data in memory
@EnableScheduling         // Enables the scheduler — required for running your reminder cron job
@EnableJpaAuditing        // Enables auditing so fields like @CreatedDate get auto-populated

public class VehicleReminderApplication {

	// Main method to run the Spring Boot application
	public static void main(String[] args) {
		SpringApplication.run(VehicleReminderApplication.class, args);
	}

}
