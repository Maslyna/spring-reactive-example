package com.example.reactive.test.integration;


import com.example.reactive.repository.AccountRepository;
import com.example.reactive.router.request.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

// NOTE: DON'T USE THIS TEST ON MAIN DATABASE!!!
//       IT WILL DROP ALL DATA!!!
@Slf4j
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {
    @Autowired
    private AccountRepository accountRepository;

    @LocalServerPort
    int port;
    @Autowired
    private WebTestClient client;

    @Test
    void createAccountWithEmptyBodyReturnsBadRequest() throws Exception {
        client.post().uri(ServiceURI.CREATE_ACCOUNT).exchange().expectStatus().is4xxClientError();
    }

    @Test
    void createAccountReturnsOk() throws Exception {
        client.post().uri(ServiceURI.CREATE_ACCOUNT)
                .bodyValue(new LoginRequest("test@mail.com", "password"))
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void createAccountReturnsConflict() throws Exception {
        createAccountReturnsOk();
        client.post().uri(ServiceURI.CREATE_ACCOUNT)
                .bodyValue(new LoginRequest("test@mail.com", "password"))
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void loginReturnsOk() throws Exception {
        createAccountReturnsOk();
        client.post().uri(ServiceURI.LOGIN)
                .bodyValue(new LoginRequest("test@mail.com", "password"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().value(HttpHeaders.AUTHORIZATION, string -> assertThat(string.startsWith("Bearer ")).isTrue());
    }

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    @AfterEach
    void prepareDataBase() {
        accountRepository.deleteAll().subscribe();
    }

    @BeforeEach
    void prepare() {
        postgreSQLContainer.start();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port).build();
    }
}
