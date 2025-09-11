package org.pragma.creditya.usecase.customer.query;

import org.pragma.creditya.model.customer.exception.CustomerDomainException;

public record VerifyOwnershipCustomerQuery(String document, String email) {
    public VerifyOwnershipCustomerQuery {
        if (document == null || document.isBlank())
            throw new CustomerDomainException("Document is mandatory");

        if (email == null || email.isBlank())
            throw new CustomerDomainException("Email is mandatory");
    }
}
