package org.pragma.creditya.r2dbc.persistence.customer;

import org.pragma.creditya.model.customer.Customer;
import org.springframework.stereotype.Component;

public class CustomerMapper  {

    public static CustomerEntity toEntity(Customer entity) {
        CustomerEntity data = new CustomerEntity();
        data.setCustomerId(entity.getId().getValue());
        data.setEmail(entity.getEmail().value());
        data.setName(entity.getFullName().name());
        data.setLastName(entity.getFullName().lastName());
        data.setBaseSalary(entity.getBaseSalary().getAmount());
        data.setPhone(entity.getPhone().value());

        return data;
    }

    public static Customer toDomain(CustomerEntity data) {
        return Customer.rebuild(
                data.getCustomerId(),
                data.getName(),
                data.getLastName(),
                data.getEmail(),
                data.getBaseSalary(),
                data.getPhone()
        );
    }


}