package com.carrental.CarAdminMonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAdminServer
@SpringBootApplication
public class CarAdminMonitorApplication {
	public static void main(String[] args) {
		SpringApplication.run(CarAdminMonitorApplication.class, args);
	}

}
