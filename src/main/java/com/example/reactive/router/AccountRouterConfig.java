package com.example.reactive.router;

import com.example.reactive.router.handler.AccountHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
@RequiredArgsConstructor
public class AccountRouterConfig {

    private final AccountHandler accountHandler;

    @Bean
    public RouterFunction<ServerResponse> personRoutes() {
        return RouterFunctions.route()
                .POST("/api/v1/account", accept(APPLICATION_JSON), accountHandler::createAccount)
                .POST("/api/v1/login", accept(APPLICATION_JSON), accountHandler::login)
                .GET("/api/v1/account", accept(APPLICATION_JSON), accountHandler::findAll)
                .GET("/api/v1/account/{id}", accept(APPLICATION_JSON), accountHandler::findById)
                .build();
    }
}