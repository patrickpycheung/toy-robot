package com.somecompany.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient configuration.
 * 
 * @author N/A
 */
@Configuration
public class WebClientConfig {
	@Bean
	public WebClient getWebClient() {
		return WebClient.create("http://localhost:8080");
	}
}
