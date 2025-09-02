package org.pragma.creditya.usecase.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.exception.CustomerDomainException;
import org.pragma.creditya.model.customer.gateways.CustomerRepository;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.usecase.customer.command.CreateCustomerCommand;
import org.pragma.creditya.usecase.customer.query.ExistDocumentQuery;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CustomerExistUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerUseCase customerUseCase;

    @BeforeEach
    void setup () {
        customerUseCase = new CustomerUseCase(customerRepository);
    }

    @Test
    void shouldThrowException_WhenDocumentIsNull () {
        ExistDocumentQuery query = new ExistDocumentQuery(null);

        StepVerifier.create(customerUseCase.queryCustomerExistByDocument(query))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Document is missed", throwable.getMessage());
                    assertInstanceOf(CustomerDomainException.class, throwable);
                })
                .verify();
    }


    @Test
    void shouldThrowException_WhenDocumentIsEmpty () {
        ExistDocumentQuery query = new ExistDocumentQuery(" ");

        StepVerifier.create(customerUseCase.queryCustomerExistByDocument(query))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Document is missed", throwable.getMessage());
                    assertInstanceOf(CustomerDomainException.class, throwable);
                })
                .verify();
    }

    @ParameterizedTest
    @CsvSource({
            "123, false",
            "222, true"
    })
    void shouldBeSuccessful (String document, Boolean expected) {
        Mockito.when(customerRepository.existByDocument(document))
                .thenReturn(Mono.just(expected));

        StepVerifier.create(customerUseCase.queryCustomerExistByDocument(new ExistDocumentQuery(document)))
                .expectNext(expected)
                .verifyComplete();
    }

}
