package com.example.reactive.service;

import com.example.reactive.entity.Account;
import com.example.reactive.exception.GlobalServiceException;
import com.example.reactive.repository.AccountRepository;
import com.example.reactive.router.request.LoginRequest;
import com.example.reactive.router.response.TokenResponse;
import com.example.reactive.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final R2dbcEntityTemplate template;

    public Mono<Account> save(LoginRequest request) {
        return repository.existsByUsernameIgnoreCase(request.username())
                .flatMap(exists -> {
                    if (!exists) {
                        Account account = Account.builder()
                                .username(request.username())
                                .password(passwordEncoder.encode(request.password()))
                                .build();

                        return template.insert(account);
                    } else {
                        return Mono.error(new GlobalServiceException(HttpStatus.CONFLICT, "user with this email already exists"));
                    }
                });
    }


    public Mono<Page<Account>> getPage(Pageable pageable) {
        return repository.findBy(pageable)
                .collectList()
                .zipWith(repository.count())
                .map(persons -> new PageImpl<>(persons.getT1(), pageable, persons.getT2()));
    }

    public Mono<Account> findById(UUID id) {
        return repository.findById(id);
    }

    public Mono<String> login(String login, String password) {
        return repository.findByUsername(login)
                .switchIfEmpty(Mono.error(new GlobalServiceException(NOT_FOUND, "User not found")))
                .flatMap(user -> passwordEncoder.matches(password, user.getPassword())
                        ? Mono.just(jwtProvider.generateToken(user))
                        : Mono.error(new GlobalServiceException(FORBIDDEN, "Invalid password")));
    }
}
