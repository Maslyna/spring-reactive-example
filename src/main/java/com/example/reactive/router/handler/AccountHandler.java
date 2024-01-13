package com.example.reactive.router.handler;

import com.example.reactive.exception.GlobalServiceException;
import com.example.reactive.router.mapper.AccountMapper;
import com.example.reactive.router.request.LoginRequest;
import com.example.reactive.service.AccountService;
import com.example.reactive.utils.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.example.reactive.router.handler.HandlerUtils.createResponse;
import static com.example.reactive.router.handler.HandlerUtils.extractPageRequest;
import static org.springframework.http.HttpStatus.*;


@Component
@RequiredArgsConstructor
public class AccountHandler {
    private final AccountMapper mapper;
    private final AccountService service;
    private final ObjectValidator validator;


    public Mono<ServerResponse> createAccount(final ServerRequest request) {

        return request.bodyToMono(LoginRequest.class)
                .switchIfEmpty(Mono.error(new GlobalServiceException(BAD_REQUEST, "request does not contains body")))
                .map(validator::validate)
                .flatMap(service::save)
                .flatMap(account -> createResponse(CREATED))
                .onErrorResume(GlobalServiceException.class, HandlerUtils::createResponse);
    }

    public Mono<ServerResponse> login(final ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .switchIfEmpty(Mono.error(new GlobalServiceException(BAD_REQUEST, "request does not contains body")))
                .map(validator::validate)
                .flatMap(loginRequest -> service.login(loginRequest.username(), loginRequest.password()))
                .flatMap(token -> ServerResponse.ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build())
                .onErrorResume(GlobalServiceException.class, HandlerUtils::createResponse);
    }

    public Mono<ServerResponse> findAll(final ServerRequest request) {
        return Mono.fromCallable(() -> extractPageRequest(request))
                .flatMap(service::getPage)
                .map(page -> page.map(mapper::accountToAccountResponse))
                .flatMap(HandlerUtils::createResponse);
    }

    public Mono<ServerResponse> findById(final ServerRequest request) {
        return Mono.fromCallable(() -> UUID.fromString(request.pathVariable("id")))
                .flatMap(service::findById)
                .map(mapper::accountToAccountResponse)
                .flatMap(person -> createResponse(OK, person))
                .onErrorResume(IllegalArgumentException.class, e -> createResponse(BAD_REQUEST, e))
                .onErrorResume(GlobalServiceException.class, HandlerUtils::createResponse);
    }
}

