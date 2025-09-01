package org.pragma.creditya.api.dto.request;

import java.math.BigDecimal;

public record CreateCustomerRequest (
        String name,
        String lastName,
        String email,
        String phone,
        BigDecimal baseSalary,
        String document
) { }
