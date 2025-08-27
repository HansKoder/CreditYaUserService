package org.pragma.creditya.model.customer;

import lombok.Getter;
import org.pragma.creditya.model.customer.valueobject.*;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Customer extends AggregateRoot<CustomerId> {

    private final Email email;
    private final FullName fullName;
    private final BaseSalary baseSalary;
    private Phone phone;

    private Customer (CustomerId customerId, FullName fullName, Email email, BaseSalary baseSalary, Phone phone) {
        setId(customerId);
        this.fullName = fullName;
        this.email = email;
        this.baseSalary = baseSalary;
        this.phone = phone;
    }

    public static Customer create(String name, String lastName, String email, BigDecimal baseSalary, String phone) {
        return new Customer(
                new CustomerId(UUID.randomUUID()),
                new FullName(name, lastName),
                new Email(email),
                new BaseSalary(baseSalary),
                new Phone(phone)
        );
    }

    public static Customer rebuild (UUID customerId, String name, String lastName, String email, BigDecimal baseSalary, String phone) {
        return new Customer(
                new CustomerId(customerId),
                new FullName(name, lastName),
                new Email(email),
                new BaseSalary(baseSalary),
                new Phone(phone)
        );
    }
}
