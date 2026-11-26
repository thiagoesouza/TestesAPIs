package com.example.bookapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server developmentServer = new Server()
                //.url("http://localhost:8080")
                .url("/")
                .description("Ambiente de desenvolvimento");

        Server productionServer = new Server()
                .url("https://api.exemplo.com")
                .description("Ambiente de produção (exemplo)");

        Contact contact = new Contact()
                .name("Equipe de Desenvolvimento")
                .email("desenvolvimento@exemplo.com");

        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0");

        Info info = new Info()
                .title("Biblioteca Virtual API")
                .version("1.0.0")
                .description("API REST da Biblioteca Virtual para cadastro, consulta, atualização e remoção de livros do acervo.")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(developmentServer, productionServer));
    }
}