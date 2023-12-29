package com.example.reactive;

import com.example.reactive.entity.Person;
import com.example.reactive.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.UUID;
import java.util.logging.Level;

@SpringBootApplication
@Slf4j
public class SpringReactiveTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringReactiveTestApplication.class, args);
    }
}
