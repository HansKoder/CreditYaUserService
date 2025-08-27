package org.pragma.creditya.usecase.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.customer.exception.CustomerDomainException;
import org.pragma.creditya.model.customer.gateways.CustomerRepository;
import org.pragma.creditya.usecase.customer.command.CreateCustomerCommand;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerUseCase customerUseCase;

    @BeforeEach
    void setup () {
        customerUseCase = new CustomerUseCase(customerRepository);
    }

    @Test
    void shouldThrowExceptionWhenNameIsRequired () {
        CreateCustomerCommand cmd = new CreateCustomerCommand(null, "doe", "doe.@gmail.com", null, BigDecimal.valueOf(1));
        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Name is mandatory", throwable.getMessage());
                    assertInstanceOf(CustomerDomainException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenLastNameIsRequired () {
        CreateCustomerCommand cmd = new CreateCustomerCommand("doe", " ", "doe.@gmail.com", null, BigDecimal.valueOf(1));
        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("LastName is mandatory", throwable.getMessage());
                    assertInstanceOf(CustomerDomainException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenEmailIsRequired () {
        CreateCustomerCommand cmd = new CreateCustomerCommand("doe", "doe", " ", null, BigDecimal.valueOf(1));
        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Email is mandatory", throwable.getMessage());
                    assertInstanceOf(CustomerDomainException.class, throwable);
                })
                .verify();
    }

    @Test
    void shouldThrowExceptionWhenEmailHasInvalidFormat () {
        CreateCustomerCommand cmd = new CreateCustomerCommand("doe", "doe", "doe.123", null, BigDecimal.valueOf(1));
        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("The email format is invalid, must have this structure example@account.com", throwable.getMessage());
                    assertInstanceOf(CustomerDomainException.class, throwable);
                })
                .verify();
    }


    @Test
    void shouldThrowExceptionWhenBaseSalaryIsNegative () {
        CreateCustomerCommand cmd = new CreateCustomerCommand("doe", "doe", "doe@gmail.com", null, BigDecimal.valueOf(-100));
        StepVerifier.create(customerUseCase.createCustomer(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Base Salary Must be positive", throwable.getMessage());
                    assertInstanceOf(CustomerDomainException.class, throwable);
                })
                .verify();
    }

}
