package org.pragma.creditya.model.customer.exception;

import org.pragma.creditya.model.shared.domain.exception.DomainException;

public class EmailUsedByOtherUserException extends DomainException {
    public EmailUsedByOtherUserException(String email) {
        super("This email " + email + " is used by other user, You need to checkt it");
    }
}
