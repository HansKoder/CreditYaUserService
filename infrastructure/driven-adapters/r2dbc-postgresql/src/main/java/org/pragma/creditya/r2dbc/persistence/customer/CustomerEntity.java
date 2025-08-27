package org.pragma.creditya.r2dbc.persistence.customer;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "customers", schema = "public")
@Getter
@Setter
@ToString
public class CustomerEntity {

    @Id
    @Column(value = "customer_id")
    private UUID customerId;

    private String name;

    @Column(value = "last_name")
    private String lastName;
    private String phone;
    private String email;

    @Column(value = "base_salary")
    private BigDecimal baseSalary;
}
