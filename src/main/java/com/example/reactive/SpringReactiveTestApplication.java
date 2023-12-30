package com.example.reactive;

import com.example.reactive.entity.Person;
import com.example.reactive.entity.Role;
import com.example.reactive.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class SpringReactiveTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringReactiveTestApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(PersonService service) {
        return args -> {
            for (int i = 0; i < 100; i++) {
                service.save(
                        Person.builder()
                                .username("username " + i)
                                .password("password")
                                .role(Role.USER)
                                .build()
                ).subscribe();
            }
        };
    }
}
