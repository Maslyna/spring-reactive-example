package com.example.reactive.router.handler;

import com.example.reactive.entity.Account;
import com.example.reactive.exception.GlobalServiceException;
import com.example.reactive.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.example.reactive.router.handler.HandlerUtils.extractPageRequest;

@Component
@RequiredArgsConstructor
public class AccountHandler {

    private final AccountService service;

    public Mono<ServerResponse> createAccount(final ServerRequest request) {
        return request.bodyToMono(Account.class)
                .flatMap(service::save)
                .flatMap(savedPerson -> ServerResponse.status(HttpStatus.CREATED).bodyValue(savedPerson));
    }

    public Mono<ServerResponse> findAll(final ServerRequest request) {
        PageRequest pageRequest;
        try {
            pageRequest = extractPageRequest(request);
        } catch (Exception e) {
            throw new GlobalServiceException(HttpStatus.BAD_REQUEST, "pagination parse error", e);
        }
        return ServerResponse.ok().body(service.getPage(pageRequest), Page.class);
    }

    public Mono<ServerResponse> findById(final ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("id"));
        return service.findById(id)
                .flatMap(person -> ServerResponse.ok().bodyValue(person))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}