package com.carrental.CarService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class CarServiceApplication implements CommandLineRunner {

    @Value("${car.image.upload-dir}")
    private String uploadDir;

    public static void main(String[] args) {
        SpringApplication.run(CarServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        try {
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Upload directory created at: " + path.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to create upload directory: " + e.getMessage());
        }
    }
}
