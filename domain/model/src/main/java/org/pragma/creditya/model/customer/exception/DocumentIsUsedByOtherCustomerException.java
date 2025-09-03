package org.pragma.creditya.model.customer.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class DocumentIsUsedByOtherCustomerException extends DomainException {
    public DocumentIsUsedByOtherCustomerException(String document) {
        super("This Document " + document + " is used by other user, You need to check it");
    }
}
