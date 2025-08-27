package org.pragma.creditya.usecase.customer.command;

import java.math.BigDecimal;

public record CreateCustomerCommand (
        String name,
        String lastName,
        String email,
        String phone,
        BigDecimal baseSalary
) { }
