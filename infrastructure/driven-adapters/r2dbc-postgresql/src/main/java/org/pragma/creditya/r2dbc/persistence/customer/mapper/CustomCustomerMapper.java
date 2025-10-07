package org.pragma.creditya.r2dbc.persistence.customer.mapper;

import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.r2dbc.helper.CustomMapper;
import org.pragma.creditya.r2dbc.persistence.customer.entity.CustomerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomCustomerMapper implements CustomMapper<Customer, CustomerEntity> {

    private final Logger logger = LoggerFactory.getLogger(CustomCustomerMapper.class);

    @Override
    public CustomerEntity toData(Customer entity) {
        logger.info("[infra.r2dbc.mapper] (CustomerMapper) toData, payload=[ entity:{} ]", entity);

        CustomerEntity data = new CustomerEntity();
        data.setCustomerId(entity.getId().getValue());
        data.setEmail(entity.getEmail().value());
        data.setName(entity.getFullName().name());
        data.setLastName(entity.getFullName().lastName());
        data.setBaseSalary(entity.getBaseSalary().getAmount());
        data.setPhone(entity.getPhone().value());
        data.setDocument(entity.getDocument().value());

        return data;
    }

    @Override
    public Customer toEntity(CustomerEntity data) {
        logger.info("[infra.r2dbc.mapper] (CustomerMapper) toEntity, payload=[ data:{} ]", data);

        return Customer.rebuild(
                data.getCustomerId(),
                data.getName(),
                data.getLastName(),
                data.getEmail(),
                data.getBaseSalary(),
                data.getDocument(),
                data.getPhone()
        );
    }
}