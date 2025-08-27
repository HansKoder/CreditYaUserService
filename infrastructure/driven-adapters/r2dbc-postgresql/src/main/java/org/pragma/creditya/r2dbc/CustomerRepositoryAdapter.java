package org.pragma.creditya.r2dbc;

import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.gateways.CustomerRepository;
import org.pragma.creditya.r2dbc.entity.CustomerEntity;
import org.pragma.creditya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class CustomerRepositoryAdapter extends ReactiveAdapterOperations<
        Customer,
        CustomerEntity,
        UUID,
        CustomerReactiveRepository
> implements CustomerRepository  {
    public CustomerRepositoryAdapter(CustomerReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Customer.class));
    }

}
