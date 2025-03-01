package com.example.MPMT;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.MPMT.service.DataGeneratorService;

@SpringBootApplication
public class MpmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpmtApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(DataGeneratorService dataGeneratorService) {
		return args -> {
			if (args.length > 0 && "generate-data".equals(args[0])) {
				dataGeneratorService.generateFakeData();
				System.out.println("Données factices générées avec succès !");
			}
		};
	}

}
