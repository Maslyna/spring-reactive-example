package com.example.reactive.controller;

import com.example.reactive.entity.Person;
import com.example.reactive.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
* Functional Endpoints, that are equals to this controller look at: <br>
* com.example.reactive.router.PersonRouterConfig
*/

//@RestController
//@RequestMapping("/api/v1/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService service;

    @PostMapping
    public Mono<Person> createPerson(
            @RequestBody Person person
    ) {
        return service.save(person);
    }

    @GetMapping
    public Flux<Person> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Person> findById(@PathVariable("id") UUID uuid) {
        return service.findById(uuid);
    }
}
