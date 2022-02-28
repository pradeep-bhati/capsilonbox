package com.demo.capsilonbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class CapsilonboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapsilonboxApplication.class, args);
	}

}
