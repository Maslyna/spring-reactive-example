package com.example.reactive.service;

import com.example.reactive.entity.Account;
import com.example.reactive.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final R2dbcEntityTemplate template;

    public Mono<Account> save(Account account) {
        return template.insert(account);
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
}
