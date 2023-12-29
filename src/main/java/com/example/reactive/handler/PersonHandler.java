package com.example.reactive.handler;

import com.example.reactive.entity.Person;
import com.example.reactive.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PersonHandler {

    private final PersonService service;

    public Mono<ServerResponse> createPerson(ServerRequest request) {
        return request.bodyToMono(Person.class)
                .flatMap(service::save)
                .flatMap(savedPerson -> ServerResponse.status(HttpStatus.CREATED).bodyValue(savedPerson));
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().body(service.getPage(PageRequest.of(0, 10)), Person.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return service.findById(id)
                .flatMap(person -> ServerResponse.ok().bodyValue(person))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}