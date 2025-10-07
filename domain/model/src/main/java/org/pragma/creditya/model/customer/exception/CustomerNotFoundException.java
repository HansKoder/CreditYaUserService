package org.pragma.creditya.model.customer.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class CustomerNotFoundException extends DomainException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
