package com.toby959.challenge;

import com.toby959.challenge.main.Main;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
		System.out.println("|||||||||||||||||||||||||||||||||||||||||||");
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main();
		main.showTheMenu();
	}
}
