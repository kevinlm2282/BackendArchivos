package com.example.archivos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.archivos.BL.FileSystemStorageService;

@SpringBootApplication
public class ArchivosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchivosApplication.class, args);
	}

	@Bean
	CommandLineRunner init(FileSystemStorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}
