package org.pragma.creditya.model.customer.valueobject;

import org.pragma.creditya.model.customer.exception.CustomerDomainException;

public record FullName(String name, String lastName) {
    public FullName {
        if (name == null || name.isBlank()) {
            throw new CustomerDomainException("Name is mandatory");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new CustomerDomainException("LastName is mandatory");
        }
    }


}
