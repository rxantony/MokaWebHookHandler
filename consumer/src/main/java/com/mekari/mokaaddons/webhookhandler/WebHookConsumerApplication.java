package com.mekari.mokaaddons.webhookhandler;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebHookConsumerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(WebHookConsumerApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		System.out.println("consumer running");
	}
}
