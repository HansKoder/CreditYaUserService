package org.pragma.creditya.usecase.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.exception.CustomerDomainException;
import org.pragma.creditya.model.customer.exception.DocumentIsUsedByOtherCustomerException;
import org.pragma.creditya.model.customer.exception.EmailUsedByOtherUserException;
import org.pragma.creditya.model.customer.gateways.CustomerRepository;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.usecase.customer.command.CreateCustomerCommand;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerUseCase customerUseCase;

    @BeforeEach
    void setup () {
        customerUseCase = new CustomerUseCase(customerRepository);
    }

    @Test
    void shouldThrowExceptionWhenNameIsRequired () {
        CreateCustomerCommand cmd = new CreateCustomerCommand(null, "doe", "doe.@gmail.com", null, BigDecimal.valueOf(1), "1142");
        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Name is mandatory", throwable.getMessage());
                    assertInstanceOf(CustomerDomainException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenLastNameIsRequired () {
        CreateCustomerCommand cmd = new CreateCustomerCommand("doe", " ", "doe.@gmail.com", null, BigDecimal.valueOf(1), "1142");
        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("LastName is mandatory", throwable.getMessage());
                    assertInstanceOf(CustomerDomainException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenEmailIsRequired () {
        CreateCustomerCommand cmd = new CreateCustomerCommand("doe", "doe", " ", null, BigDecimal.valueOf(1), "112");
        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Email is mandatory", throwable.getMessage());
                    assertInstanceOf(CustomerDomainException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenEmailHasInvalidFormat () {
        CreateCustomerCommand cmd = new CreateCustomerCommand("doe", "doe", "doe.123", null, BigDecimal.valueOf(1), "123");
        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("The email format is invalid, must have this structure example@account.com", throwable.getMessage());
                    assertInstanceOf(CustomerDomainException.class, throwable);
                })
                .verify();
    }


    @Test
    void shouldThrowExceptionWhenBaseSalaryIsNegative () {
        CreateCustomerCommand cmd = new CreateCustomerCommand("doe", "doe", "doe@gmail.com", null, BigDecimal.valueOf(-100), "113");
        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Base Salary Must be positive", throwable.getMessage());
                    assertInstanceOf(CustomerDomainException.class, throwable);
                })
                .verify();
    }


    @Test
    void shouldThrowExceptionWhenDocumentIsMissed() {
        CreateCustomerCommand cmd = new CreateCustomerCommand("doe", "doe", "doe@gmail.com", null, BigDecimal.valueOf(100), "");
        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Document is mandatory", throwable.getMessage());
                    assertInstanceOf(CustomerDomainException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenEmailIsUsedByOtherCustomer () {
        Mockito.when(customerRepository.existByEmail("doe@gmail.com"))
                .thenReturn(Mono.just(Boolean.TRUE));

        CreateCustomerCommand cmd = new CreateCustomerCommand("doe", "doe", "doe@gmail.com", null, BigDecimal.valueOf(100), "113");
        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("This email doe@gmail.com is used by other user, You need to check it",
                            throwable.getMessage());
                    assertInstanceOf(EmailUsedByOtherUserException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenDocumentIsUsedByOtherCustomer () {
        Mockito.when(customerRepository.existByEmail("doe@gmail.com"))
                .thenReturn(Mono.just(Boolean.FALSE));

        Mockito.when(customerRepository.existByDocument("113"))
                .thenReturn(Mono.just(Boolean.TRUE));

        CreateCustomerCommand cmd = new CreateCustomerCommand("doe", "doe", "doe@gmail.com", null, BigDecimal.valueOf(100), "113");
        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("This Document 113 is used by other user, You need to check it",
                            throwable.getMessage());
                    assertInstanceOf(DocumentIsUsedByOtherCustomerException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldBePersistedWithSuccessful_whenCustomersIsUniqueAndValid () {
        UUID customerId = UUID.fromString("2501d95b-e2ee-4c5f-8c1e-72ecdfb24a2b");
        Customer entity = Customer.rebuild(customerId, "doe", "doe", "doe@gmail.com", BigDecimal.TEN, "113", "");

        Mockito.when(customerRepository.existByEmail("doe@gmail.com"))
                .thenReturn(Mono.just(Boolean.FALSE));

        Mockito.when(customerRepository.existByDocument("113"))
                .thenReturn(Mono.just(Boolean.FALSE));

        Mockito.when(customerRepository.save(any(Customer.class)))
                .thenReturn(Mono.just(entity));

        CreateCustomerCommand cmd = new CreateCustomerCommand("doe", "doe", "doe@gmail.com", "", BigDecimal.TEN, "113");

        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectNext(new CustomerId(customerId))
                .verifyComplete();
    }


}
