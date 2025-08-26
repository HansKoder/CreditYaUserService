package org.pragma.creditya.model.customer.valueobject;

import org.pragma.creditya.model.customer.exception.CustomerDomainException;

import java.math.BigDecimal;
import java.util.Objects;

public class BaseSalary {

    private final BigDecimal amount;

    private static final int LIMIT_BASE_SALARY = 15000;

    public BaseSalary (BigDecimal amount) {
        this.amount = amount;
        validations();
    }

    private void validations () {
        if (amount == null)
            throw new CustomerDomainException("Base Salary must be mandatory");

        if (!isGreaterThanZero())
            throw new CustomerDomainException("Base Salary Must be positive");

        if (isGreaterThan(LIMIT_BASE_SALARY))
            throw new CustomerDomainException("Base Salary must be lower than the limit allowed");
    }


    private boolean isGreaterThanZero() {
        System.out.println("Amount " + amount);
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(int limit) {
        return this.amount != null && this.amount.compareTo(BigDecimal.valueOf(limit)) > 0;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseSalary money = (BaseSalary) o;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

}