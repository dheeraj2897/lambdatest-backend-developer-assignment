package com.dheeraj.lambdatest.assignement;

import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.dheeraj.lambdatest.assignement.service.JsonEnricherService;

@SpringBootApplication
public class JsonEnricherApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsonEnricherApplication.class, args);
	}
	
	@Bean
    public CommandLineRunner run(JsonEnricherService jsonEnricherService) {
        return args -> {
            try {
                jsonEnricherService.enrichJsonFiles();
                System.out.println("Enrichment completed. Check the output file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

}
