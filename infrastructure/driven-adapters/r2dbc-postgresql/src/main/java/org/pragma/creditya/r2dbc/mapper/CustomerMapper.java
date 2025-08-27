package org.pragma.creditya.r2dbc.mapper;

import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.r2dbc.entity.CustomerEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper implements DomainDataMapper<Customer, CustomerEntity> {

    @Override
    public CustomerEntity toData(Customer customer) {
        CustomerEntity entity = new CustomerEntity();
        entity.setCustomerId(customer.getId().getValue());
        entity.setEmail(customer.getEmail().value());
        entity.setName(customer.getFullName().name());
        entity.setLastName(customer.getFullName().lastName());
        entity.setBaseSalary(customer.getBaseSalary().getAmount());
        entity.setPhone(customer.getPhone().value());
        return entity;
    }

    @Override
    public Customer toDomain(CustomerEntity entity) {
        return Customer.rebuild(
                entity.getCustomerId(),
                entity.getName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getBaseSalary(),
                entity.getPhone()
        );
    }
}