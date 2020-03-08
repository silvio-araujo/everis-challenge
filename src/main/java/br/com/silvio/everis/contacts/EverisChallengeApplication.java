package br.com.silvio.everis.contacts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EverisChallengeApplication {

	private EverisChallengeApplication() {
		throw new IllegalStateException("State class");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(EverisChallengeApplication.class, args);
	}

}
