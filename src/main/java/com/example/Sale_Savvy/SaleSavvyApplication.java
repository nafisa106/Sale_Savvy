package com.example.Sale_Savvy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.servlet.context.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SaleSavvyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaleSavvyApplication.class, args);
	}

}
