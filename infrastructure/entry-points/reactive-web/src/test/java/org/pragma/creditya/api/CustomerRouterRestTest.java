package org.pragma.creditya.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.pragma.creditya.api.dto.request.CreateCustomerRequest;
import org.pragma.creditya.api.dto.response.CustomerIdResponse;
import org.pragma.creditya.api.dto.response.ErrorResponse;
import org.pragma.creditya.model.customer.exception.CustomerDomainException;
import org.pragma.creditya.model.customer.exception.DocumentIsUsedByOtherCustomerException;
import org.pragma.creditya.model.customer.exception.EmailUsedByOtherUserException;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.usecase.customer.ICustomerUseCase;
import org.pragma.creditya.usecase.customer.command.CreateCustomerCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CustomerRouterRest.class, CustomerHandler.class})
@WebFluxTest
class CustomerRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    ICustomerUseCase customerUseCase;

    @Test
    void shouldCreateUserWithSuccessful() {
        UUID customerId = UUID.fromString("5b87a0d6-2fed-4db7-aa49-49663f719659");
        CustomerId response = new CustomerId(customerId);

        when(customerUseCase.createCustomer(any(CreateCustomerCommand.class)))
                .thenReturn(Mono.just(response));

        CreateCustomerRequest command = new CreateCustomerRequest("doe", "doe", "doe@gmail.com", "", BigDecimal.TEN, "123");

        webTestClient.post()
                .uri("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CustomerIdResponse.class)
                .value(persisted -> {
                    assertEquals("5b87a0d6-2fed-4db7-aa49-49663f719659", persisted.customerId());
                });
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsEmpty() {
        when(customerUseCase.createCustomer(any(CreateCustomerCommand.class)))
                .thenReturn(Mono.error(new CustomerDomainException("name must be mandatory")));

        CreateCustomerRequest command = new CreateCustomerRequest("", "doe", "doe@gmail.com", "", BigDecimal.TEN, "123");

        webTestClient.post()
                .uri("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                            assertEquals(400, errorResponse.status());
                            assertEquals("name must be mandatory", errorResponse.message());
                        }
                );;

    }


    @Test
    void shouldThrowExceptionWhenEmailIsUsedByOtherCustomer() {
        when(customerUseCase.createCustomer(any(CreateCustomerCommand.class)))
                .thenReturn(Mono.error(new EmailUsedByOtherUserException("doe")));

        CreateCustomerRequest command = new CreateCustomerRequest("", "doe", "doe@gmail.com", "", BigDecimal.TEN, "123");

        webTestClient.post()
                .uri("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                            assertEquals(409, errorResponse.status());
                            assertEquals("This email doe is used by other user, You need to check it", errorResponse.message());
                        }
                );;

    }

    @Test
    void shouldThrowExceptionWhenDocumentIsUsedByOtherCustomer() {
        when(customerUseCase.createCustomer(any(CreateCustomerCommand.class)))
                .thenReturn(Mono.error(new DocumentIsUsedByOtherCustomerException("113")));

        CreateCustomerRequest command = new CreateCustomerRequest("", "doe", "doe@gmail.com", "", BigDecimal.TEN, "123");

        webTestClient.post()
                .uri("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                            assertEquals(409, errorResponse.status());
                            assertEquals("This Document 113 is used by other user, You need to check it", errorResponse.message());
                        }
                );;

    }

    @Test
    void shouldThrowExceptionWhenDBIsNotWorking() {
        when(customerUseCase.createCustomer(any(CreateCustomerCommand.class)))
                .thenReturn(Mono.error(new RuntimeException("DB is not working")));

        CreateCustomerRequest command = new CreateCustomerRequest("", "doe", "doe@gmail.com", "", BigDecimal.TEN, "123");

        webTestClient.post()
                .uri("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> {
                            assertEquals(500, errorResponse.status());
                            assertEquals("DB is not working", errorResponse.message());
                        }
                );;

    }


}
