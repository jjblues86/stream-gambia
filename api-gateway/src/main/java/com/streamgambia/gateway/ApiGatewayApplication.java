package com.streamgambia.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "http://localhost:3000") // 1. Allow Frontend to talk to us
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    // 2. HARDCODED ROUTES (Bypassing YAML configuration)
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route for Auth Service
                .route("auth-service", r -> r.path("/auth/**")
                        .uri("http://auth-service:8081"))

                // Route for Video Service (The one fixing your 404)
                .route("video-service", r -> r.path("/videos/**")
                        .uri("http://video-service:8082"))
                .build();
    }
}