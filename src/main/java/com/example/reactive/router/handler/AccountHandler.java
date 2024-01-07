package com.example.reactive.router.handler;

import com.example.reactive.entity.Account;
import com.example.reactive.exception.GlobalServiceException;
import com.example.reactive.router.request.LoginRequest;
import com.example.reactive.router.response.TokenResponse;
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
                .flatMap(savedPerson -> ServerResponse.status(HttpStatus.CREATED).bodyValue(savedPerson))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> login(final ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(loginRequest -> service.login(loginRequest.login(), loginRequest.password()))
                .flatMap(token -> ServerResponse.ok().bodyValue(new TokenResponse(token)))
                .onErrorResume(GlobalServiceException.class, e -> ServerResponse.status(e.getStatusCode()).bodyValue(e.getMessage()));
    }


    public Mono<ServerResponse> findAll(final ServerRequest request) {
        try {
            PageRequest pageRequest = extractPageRequest(request);
            return ServerResponse.ok().body(service.getPage(pageRequest), Page.class);
        } catch (Exception e) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue("Pagination parse error: " + e.getMessage());
        }
    }

    public Mono<ServerResponse> findById(final ServerRequest request) {
        try {
            UUID id = UUID.fromString(request.pathVariable("id"));
            return service.findById(id)
                    .flatMap(person -> ServerResponse.ok().bodyValue(person))
                    .switchIfEmpty(ServerResponse.notFound().build());
        } catch (IllegalArgumentException e) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue("Invalid UUID format: " + e.getMessage());
        }
    }
}

