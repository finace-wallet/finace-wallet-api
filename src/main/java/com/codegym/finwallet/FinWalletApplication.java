package com.codegym.finwallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FinWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinWalletApplication.class, args);
	}

}
