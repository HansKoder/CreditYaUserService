package org.pragma.creditya.model.customer.valueobject;

import org.pragma.creditya.model.customer.exception.CustomerDomainException;

public record Email(String value) {
    public Email {
        if (value == null || value.isBlank()) {
            throw new CustomerDomainException("Email is mandatory");
        }
        if (!value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new CustomerDomainException("The email format is invalid, must have this structure example@account.com");
        }
    }
}

