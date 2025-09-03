package org.pragma.creditya.model.customer.valueobject;

import org.pragma.creditya.model.customer.exception.CustomerDomainException;

public record Document (String value) {
    public Document {
        if (value == null || value.isBlank()) {
            throw new CustomerDomainException("Document is mandatory");
        }
    }
}
