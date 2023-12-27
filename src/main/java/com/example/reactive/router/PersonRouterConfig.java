package com.example.reactive.router;

import com.example.reactive.handler.PersonHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
@RequiredArgsConstructor
public class PersonRouterConfig {

    private final PersonHandler personHandler;

    @Bean
    public RouterFunction<ServerResponse> personRoutes() {
        return RouterFunctions.route()
                .POST("/api/v1/persons", accept(APPLICATION_JSON), personHandler::createPerson)
                .GET("/api/v1/persons", accept(APPLICATION_JSON), personHandler::findAll)
                .GET("/api/v1/persons/{id}", accept(APPLICATION_JSON), personHandler::findById)
                .build();
    }
}