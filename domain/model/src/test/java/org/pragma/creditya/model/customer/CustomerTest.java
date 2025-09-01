package org.pragma.creditya.model.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.pragma.creditya.model.customer.exception.CustomerDomainException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Test
    void shouldThrowExceptionWhenTheNameIsEmpty () {
        CustomerDomainException exception = assertThrows(CustomerDomainException.class, () -> {
            Customer.create("", "Doe", "jhon@gmail.com", BigDecimal.valueOf(-1), "114233", "(57) 301-101000" );
        });

        assertEquals("Name is mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTheNameIsNull () {
        CustomerDomainException exception = assertThrows(CustomerDomainException.class, () -> {
            Customer.create(null, "Doe", "jhon@gmail.com", BigDecimal.valueOf(-1), "114233", "(57) 301-101000");
        });

        assertEquals("Name is mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTheLastNameIsEmpty () {
        CustomerDomainException exception = assertThrows(CustomerDomainException.class, () -> {
            Customer.create("Jhon", "", "jhon@gmail.com", BigDecimal.valueOf(-1), "114233", "(57) 301-101000");
        });

        assertEquals("LastName is mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTheLastNameIsNull () {
        CustomerDomainException exception = assertThrows(CustomerDomainException.class, () -> {
            Customer.create("Jhon", null, "jhon@gmail.com", BigDecimal.valueOf(-1), "114233", "(57) 301-101000");
        });

        assertEquals("LastName is mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTheEmailIsEmpty () {
        CustomerDomainException exception = assertThrows(CustomerDomainException.class, () -> {
            Customer.create("Jhon", "Doe", "", BigDecimal.valueOf(-1), "114233","(57) 301-101000");
        });

        assertEquals("Email is mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTheEmailIsNull () {
        CustomerDomainException exception = assertThrows(CustomerDomainException.class, () -> {
            Customer.create("Jhon", "Doe", null, BigDecimal.valueOf(-1), "114233", "(57) 301-101000");
        });

        assertEquals("Email is mandatory", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTheEmailHasAnInvalidFormat () {
        CustomerDomainException exception = assertThrows(CustomerDomainException.class, () -> {
            Customer.create("Jhon", "Doe", "jhon.gmail", BigDecimal.valueOf(-1), "114233", "(57) 301-101000");
        });

        assertEquals("The email format is invalid, must have this structure example@account.com", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTheBaseSalaryIsNull() {
        CustomerDomainException exception = assertThrows(CustomerDomainException.class, () -> {
            Customer.create("Jhon", "Doe", "jhon.doe@gmail.com", null, "114233", "(57) 301-101000");
        });

        assertEquals("Base Salary must be mandatory", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100, Integer.MIN_VALUE})
    void shouldThrowExceptionWhenTheBaseSalaryIsNegative (int amount) {
        CustomerDomainException exception = assertThrows(CustomerDomainException.class, () -> {
            Customer.create("Jhon", "Doe", "jhon.doe@gmail.com", BigDecimal.valueOf(amount), "114233", "(57) 301-101000");
        });

        assertEquals("Base Salary Must be positive", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {15001, 300000, Integer.MAX_VALUE})
    void shouldThrowExceptionWhenTheBaseSalaryIsGreaterThanLimit (int amount) {
        CustomerDomainException exception = assertThrows(CustomerDomainException.class, () -> {
            Customer.create("Jhon", "Doe", "jhon.doe@gmail.com", BigDecimal.valueOf(amount), "114233", "(57) 301-101000");
        });

        assertEquals("Base Salary must be lower than the limit allowed", exception.getMessage());
    }

    @Test
    void shouldCreateUserWithSuccess () {
        Customer customer = Customer.create("Jhon", "Doe", "jhon.doe@gmail.com", BigDecimal.valueOf(1000), "114233","(57) 301-101000");
        assertNotNull(customer);
        assertNotNull(customer.getId());
    }


}
