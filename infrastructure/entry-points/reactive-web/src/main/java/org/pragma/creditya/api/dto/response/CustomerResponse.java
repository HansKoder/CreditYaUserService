package org.pragma.creditya.api.dto.response;

import java.math.BigDecimal;

public record CustomerResponse (
    String document,
    String email,
    BigDecimal baseSalary
) { }
