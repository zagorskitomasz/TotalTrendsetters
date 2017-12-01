package com.zagorskidev.graphnet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Launcher for Spring Boot web app.
 * 
 * @author Tomasz Zagórski
 *
 */
@EnableAsync
@SpringBootApplication
public class GraphnetApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphnetApplication.class, args);
	}
}
