package com.example.reactive.handler;

import com.example.reactive.entity.Person;
import com.example.reactive.exception.GlobalServiceException;
import com.example.reactive.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    public Mono<ServerResponse> createPerson(final ServerRequest request) {
        return request.bodyToMono(Person.class)
                .flatMap(service::save)
                .flatMap(savedPerson -> ServerResponse.status(HttpStatus.CREATED).bodyValue(savedPerson));
    }

    public Mono<ServerResponse> findAll(final ServerRequest request) {
        try {
            PageRequest pageRequest = extractPageRequest(request);
            return ServerResponse.ok().body(service.getPage(pageRequest), Page.class);
        } catch (Exception e) {
            throw new GlobalServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public Mono<ServerResponse> findById(final ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return service.findById(id)
                .flatMap(person -> ServerResponse.ok().bodyValue(person))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private PageRequest extractPageRequest(final ServerRequest request) {
        int page = request.queryParam("page").map(Integer::parseUnsignedInt).orElse(0);
        int size = request.queryParam("size").map(Integer::parseUnsignedInt).orElse(5);

        List<String> sortBy = request.queryParams().get("sortBy");
        Sort.Direction direction = request.queryParam("direction")
                .map(Sort.Direction::fromString)
                .orElse(Sort.Direction.DESC);

        if (sortBy != null && !sortBy.isEmpty()) {
            return PageRequest.of(page, size, direction, sortBy.toArray(new String[0]));
        } else {
            return PageRequest.of(page, size);
        }
    }
}