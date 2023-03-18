package com.ipasoft.hazelcast.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI springBootOpenApi() throws IOException {
		return new OpenAPI().info( apiInfo() );
	}
	
	private Info apiInfo() throws IOException {
        return new Info().title("Ipa Api")
						 .description("Hazelcast Demo")
						 .version("1.0.1")
						 .license( new License().name("ipasoft"));
    }
}