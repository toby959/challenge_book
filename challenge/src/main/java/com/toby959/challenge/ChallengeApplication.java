package com.toby959.challenge;

import com.toby959.challenge.main.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeApplication implements CommandLineRunner {


	private final Main main;

	@Autowired
    public ChallengeApplication(Main main) {
        this.main = main;
    }

    public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);

	}

@Override
public void run(String... args) {
	main.showTheMenu();
  }
}
