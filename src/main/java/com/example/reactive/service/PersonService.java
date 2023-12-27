package com.example.reactive.service;

import com.example.reactive.entity.Person;
import com.example.reactive.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository repository;

    public Mono<Person> save(Person person) {
        return repository.save(person);
    }

    public Flux<Person> findAll() {
        return repository.findAll()
                .delayElements(Duration.of(1, ChronoUnit.SECONDS));
    }

    public Mono<Person> findById(UUID id) {
        return repository.findById(id);
    }
}
