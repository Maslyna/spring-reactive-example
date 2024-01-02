package com.example.reactive.controller;

import com.example.reactive.entity.Account;
import com.example.reactive.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
* Functional Endpoints, that are equals to this controller look at: <br>
* com.example.reactive.router.PersonRouterConfig
*/

//@RestController
//@RequestMapping("/api/v1/persons")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @PostMapping
    public Mono<Account> createPerson(
            @RequestBody Account account
    ) {
        return service.save(account);
    }

    @GetMapping
    public Mono<Page<Account>> findAll() {
        return service.getPage(PageRequest.of(0, 10));
    }

    @GetMapping("/{id}")
    public Mono<Account> findById(@PathVariable("id") UUID uuid) {
        return service.findById(uuid);
    }
}
