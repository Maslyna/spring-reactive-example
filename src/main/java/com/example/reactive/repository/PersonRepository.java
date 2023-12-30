package com.example.reactive.repository;

import com.example.reactive.entity.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface PersonRepository extends ReactiveCrudRepository<Person, UUID> {
    Flux<Person> findBy(Pageable pageable);
}