package org.pragma.creditya.model.customer;

import lombok.Getter;
import lombok.ToString;
import org.pragma.creditya.model.customer.valueobject.*;
import org.pragma.creditya.model.shared.domain.model.entity.AggregateRoot;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@ToString
public class Customer extends AggregateRoot<CustomerId> {

    private final Email email;
    private final FullName fullName;
    private final BaseSalary baseSalary;

    private final Document document;

    private Phone phone;

    private Customer (CustomerId customerId, FullName fullName, Email email, BaseSalary baseSalary, Document document, Phone phone) {
        setId(customerId);
        this.fullName = fullName;
        this.email = email;
        this.baseSalary = baseSalary;
        this.phone = phone;
        this.document = document;
    }

    public static Customer create(String name, String lastName, String email, BigDecimal baseSalary, String document, String phone) {
        return new Customer(
                new CustomerId(UUID.randomUUID()),
                new FullName(name, lastName),
                new Email(email),
                new BaseSalary(baseSalary),
                new Document(document),
                new Phone(phone)
        );
    }

    public static Customer rebuild (UUID customerId, String name, String lastName, String email, BigDecimal baseSalary, String document, String phone) {
        return new Customer(
                new CustomerId(customerId),
                new FullName(name, lastName),
                new Email(email),
                new BaseSalary(baseSalary),
                new Document(document),
                new Phone(phone)
        );
    }
}
