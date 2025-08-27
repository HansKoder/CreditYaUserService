package org.pragma.creditya.r2dbc.persistence.customer.mapper;

import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.r2dbc.helper.CustomMapper;
import org.pragma.creditya.r2dbc.persistence.customer.entity.CustomerEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomCustomerMapper implements CustomMapper<Customer, CustomerEntity> {

    @Override
    public CustomerEntity toData(Customer entity) {
        CustomerEntity data = new CustomerEntity();
        data.setCustomerId(entity.getId().getValue());
        data.setEmail(entity.getEmail().value());
        data.setName(entity.getFullName().name());
        data.setLastName(entity.getFullName().lastName());
        data.setBaseSalary(entity.getBaseSalary().getAmount());
        data.setPhone(entity.getPhone().value());

        return data;
    }

    @Override
    public Customer toEntity(CustomerEntity data) {
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