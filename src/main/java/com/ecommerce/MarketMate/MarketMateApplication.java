package com.ecommerce.MarketMate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MarketMateApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext asc = SpringApplication.run(MarketMateApplication.class, args);

		String[] beans = asc.getBeanDefinitionNames();

		for (String s : beans) {
			System.out.println(s);
		}
	}

}
