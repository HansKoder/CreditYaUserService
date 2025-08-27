package org.pragma.creditya.r2dbc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
@ToString
public class CustomerEntity {

    @Id
    @Column(name = "customer_id")
    private UUID customerId;

    private String name;

    @Column(name = "last_name")
    private String lastName;
    private String phone;
    private String email;

    @Column(name = "base_salary")
    private BigDecimal baseSalary;
}
