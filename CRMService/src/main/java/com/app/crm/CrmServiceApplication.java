package com.app.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaRepositories
public class CrmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmServiceApplication.class, args);
	}
	
	@Bean
	public RestTemplate rt()
	{
		return new RestTemplate();
	}

}
