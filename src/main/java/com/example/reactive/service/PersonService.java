package com.example.reactive.service;

import com.example.reactive.entity.Person;
import com.example.reactive.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.relational.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository repository;
    private final R2dbcEntityTemplate template;

    public Mono<Person> save(Person person) {
        return template.insert(person);
    }

    public Mono<Page<Person>> getPage(Pageable pageable) {
        return repository.findBy(pageable);
    }

    public Mono<Person> findById(UUID id) {
        return repository.findById(id);
    }
}
