package com.example.reactive.handler;

import com.example.reactive.entity.Person;
import com.example.reactive.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
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
        PageRequest pageRequest;
        int page;
        int size;

        List<String> sortBy;
        Sort.Direction direction;
        try {
            page = request.queryParam("page").map(Integer::parseUnsignedInt).orElse(0);
            size = request.queryParam("size").map(Integer::parseUnsignedInt).orElse(5);
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest().bodyValue("Invalid value for 'page' or 'size'");
        }
        sortBy = request.queryParams().get("sortBy");
        direction = request.queryParam("direction")
                .map(Sort.Direction::fromString)
                .orElse(Sort.Direction.DESC);

        if (sortBy != null && !sortBy.isEmpty()) {
            pageRequest = PageRequest.of(page, size, direction, sortBy.toArray(new String[0]));
        } else {
            pageRequest = PageRequest.of(page, size, direction);
        }

        return ServerResponse.ok().body(service.getPage(pageRequest), Person.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return service.findById(id)
                .flatMap(person -> ServerResponse.ok().bodyValue(person))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}