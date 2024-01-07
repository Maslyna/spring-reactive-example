package com.example.reactive.repository;

import com.example.reactive.entity.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AccountRepository extends ReactiveCrudRepository<Account, UUID> {
    Mono<Boolean> existsByUsernameIgnoreCase(String username);

    Mono<Account> findByUsername(String username);
    Flux<Account> findBy(Pageable pageable);

}