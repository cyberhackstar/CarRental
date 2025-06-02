package com.carrental.CarListnerService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class CarListnerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarListnerServiceApplication.class, args);
	}

}
