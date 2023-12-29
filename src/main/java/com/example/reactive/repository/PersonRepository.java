package com.example.reactive.repository;

import com.example.reactive.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PersonRepository extends ReactiveCrudRepository<Person, UUID> {

    Mono<Person> findByUsername(String username);

    Mono<Page<Person>> findBy(Pageable pageable);
}