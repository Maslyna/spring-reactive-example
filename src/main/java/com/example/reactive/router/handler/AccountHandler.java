package com.example.reactive.router.handler;

import com.example.reactive.exception.GlobalServiceException;
import com.example.reactive.router.request.LoginRequest;
import com.example.reactive.service.AccountService;
import com.example.reactive.utils.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.example.reactive.router.handler.HandlerUtils.createResponse;
import static com.example.reactive.router.handler.HandlerUtils.extractPageRequest;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class AccountHandler {
    private final AccountService service;
    private final ObjectValidator validator;

    public Mono<ServerResponse> createAccount(final ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .map(validator::validate)
                .flatMap(service::save)
                .flatMap(savedPerson -> ServerResponse.status(CREATED).bodyValue(savedPerson))
                .onErrorResume(GlobalServiceException.class,
                        e -> ServerResponse.status(e.getStatusCode()).contentType(APPLICATION_JSON).bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> login(final ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .map(validator::validate)
                .flatMap(loginRequest -> service.login(loginRequest.username(), loginRequest.password()))
                .flatMap(token -> ServerResponse.ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build())
                .onErrorResume(GlobalServiceException.class,
                        e -> ServerResponse.status(e.getStatusCode()).contentType(APPLICATION_JSON).bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> findAll(final ServerRequest request) {
        try {
            PageRequest pageRequest = extractPageRequest(request);
            return ServerResponse.ok().body(service.getPage(pageRequest), Page.class);
        } catch (Exception e) {
            return ServerResponse.status(BAD_REQUEST).contentType(APPLICATION_JSON).bodyValue("Pagination parse error: " + e.getMessage());
        }
    }

    public Mono<ServerResponse> findById(final ServerRequest request) {
        return Mono.just(UUID.fromString(request.pathVariable("id")))
                .flatMap(service::findById)
                .flatMap(person -> createResponse(HttpStatus.OK, person))
                .onErrorResume(err -> {
                    if (err instanceof GlobalServiceException e) {
                        return createResponse(e);
                    } else if (err instanceof IllegalArgumentException e) {
                        return createResponse(BAD_REQUEST, e.getMessage());
                    }
                    return createResponse(BAD_REQUEST, null);
                });
    }
}

