package org.pragma.creditya.api;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.api.dto.response.ErrorResponse;
import org.pragma.creditya.api.dto.response.ExistCustomerDocumentResponse;
import org.pragma.creditya.usecase.customer.ICustomerUseCase;
import org.pragma.creditya.usecase.customer.query.ExistDocumentQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CustomerRouterRest.class, CustomerHandler.class})
@WebFluxTest
class ExistCustomerRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    ICustomerUseCase customerUseCase;

    private final String DOCUMENT = "123";

    @Test
    void shouldBeOK_CustomerExists() {

        when(customerUseCase.queryCustomerExistByDocument(any(ExistDocumentQuery.class)))
                .thenReturn(Mono.just(Boolean.TRUE));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/exists")
                        .queryParam("document", DOCUMENT)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExistCustomerDocumentResponse.class)
                .value(response -> {
                    assertTrue(response.exists());
                });
    }

    @Test
    void shouldThrowException_WhenDocumentIsNullOrEmpty() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/exists")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                            assertEquals(500, errorResponse.status());
                            assertEquals("Document must be mandatory", errorResponse.message());
                        }
                );;

    }

    @Test
    void shouldThrowException_WhenDBIsNotWorking() {
        when(customerUseCase.queryCustomerExistByDocument(any(ExistDocumentQuery.class)))
                .thenReturn(Mono.error(new SQLException("DB is not working")));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/exists")
                        .queryParam("document", DOCUMENT)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                            assertEquals(500, errorResponse.status());
                            assertEquals("DB is not working", errorResponse.message());
                        }
                );;

    }

    @Test
    void shouldBeOK_WhenCustomerDoesNotExist() {
        when(customerUseCase.queryCustomerExistByDocument(any(ExistDocumentQuery.class)))
                .thenReturn(Mono.just(Boolean.FALSE));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/exists")
                        .queryParam("document", DOCUMENT)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExistCustomerDocumentResponse.class)
                .value(response -> {
                    assertFalse(response.exists());
                });
    }

}
