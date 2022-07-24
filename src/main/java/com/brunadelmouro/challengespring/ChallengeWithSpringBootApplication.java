package com.brunadelmouro.challengespring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChallengeWithSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeWithSpringBootApplication.class, args);
	}

}
