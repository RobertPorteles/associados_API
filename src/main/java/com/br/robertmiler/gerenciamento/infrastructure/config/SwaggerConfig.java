package com.br.robertmiler.gerenciamento.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Gestão de Associados API")
						.description("API para gerenciamento de associados, equipes, empresas e endereços")
						.version("v1.0.0"));
	}

}
