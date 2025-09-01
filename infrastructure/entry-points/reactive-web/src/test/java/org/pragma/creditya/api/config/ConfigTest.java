package org.pragma.creditya.api.config;

import org.pragma.creditya.api.CustomerHandler;
import org.pragma.creditya.api.CustomerRouterRest;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.usecase.customer.ICustomerUseCase;
import org.pragma.creditya.usecase.customer.command.CreateCustomerCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CustomerRouterRest.class, CustomerHandler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    ICustomerUseCase customerUseCase;

    @Test
    void corsConfigurationShouldAllowOrigins() {

        UUID customerId = UUID.fromString("5b87a0d6-2fed-4db7-aa49-49663f719659");
        CustomerId response = new CustomerId(customerId);

        when(customerUseCase.createCustomer(any(CreateCustomerCommand.class)))
                .thenReturn(Mono.just(response));

        webTestClient.post()
                .uri("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateCustomerCommand("doe", "doe", "doe@gmail.com", "", BigDecimal.TEN, "123"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}