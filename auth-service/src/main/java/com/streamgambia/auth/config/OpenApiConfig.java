package com.streamgambia.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "StreamGambia Auth API",
                version = "1.0",
                description = "API for handling User Registration and Login operations",
                contact = @Contact(name = "StreamGambia Dev", email = "dev@streamgambia.com")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Environment")
        }
)
public class OpenApiConfig {
}
