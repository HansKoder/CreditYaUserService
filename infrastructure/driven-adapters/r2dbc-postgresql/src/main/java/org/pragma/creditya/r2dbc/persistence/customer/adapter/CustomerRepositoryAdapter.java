package org.pragma.creditya.r2dbc.persistence.customer.adapter;

import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.r2dbc.helper.ReactiveAdapterOperations;
import org.pragma.creditya.r2dbc.persistence.customer.entity.CustomerEntity;
import org.pragma.creditya.r2dbc.persistence.customer.mapper.CustomCustomerMapper;
import org.pragma.creditya.r2dbc.persistence.customer.repository.CustomerReactiveRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public class CustomerRepositoryAdapter extends ReactiveAdapterOperations<
        Customer/* change for domain model */,
        CustomerEntity/* change for adapter model */,
        UUID,
        CustomerReactiveRepository
        > {
    public CustomerRepositoryAdapter(CustomerReactiveRepository repository, CustomCustomerMapper mapper) {
        super(repository, mapper, mapper::toEntity);
    }

}
