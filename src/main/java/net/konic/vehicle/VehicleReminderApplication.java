package net.konic.vehicle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class VehicleReminderApplication {

	// Main method to run the Spring Boot application
	public static void main(String[] args) {
		SpringApplication.run(VehicleReminderApplication.class, args);
	}

}
