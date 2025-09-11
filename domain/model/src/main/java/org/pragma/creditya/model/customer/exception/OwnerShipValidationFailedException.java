package org.pragma.creditya.model.customer.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class OwnerShipValidationFailedException extends DomainException {
    public OwnerShipValidationFailedException(String message) {
        super(message);
    }
}
