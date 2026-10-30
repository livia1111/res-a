package com.seupacote.ms1.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração global do OpenAPI 3 / Swagger UI.
 * Acesse: http://localhost:8081/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS1 – Usuários API")
                        .description("""
                            Microserviço de CRUD de usuários com **arquitetura hexagonal**.

                            **Fluxo de autenticação:**
                            1. Crie um usuário em `POST /users`
                            2. Faça login em `POST /auth/login` e copie o token
                            3. Clique em **Authorize** e cole `Bearer <token>`
                            4. Acesse os demais endpoints
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe MS1")
                                .email("dev@seupacote.com")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT — obtenha via POST /auth/login")));
    }
}

