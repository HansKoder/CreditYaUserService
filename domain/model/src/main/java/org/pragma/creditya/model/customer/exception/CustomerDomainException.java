package org.pragma.creditya.model.customer.exception;

public class CustomerDomainException extends RuntimeException{
    public CustomerDomainException() {
    }

    public CustomerDomainException(String message) {
        super(message);
    }
}
