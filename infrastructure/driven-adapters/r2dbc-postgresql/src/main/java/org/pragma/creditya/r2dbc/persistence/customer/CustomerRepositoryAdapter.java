package org.pragma.creditya.r2dbc.persistence.customer;

import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.r2dbc.helper.ReactiveAdapterOperations;
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
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, mapper::toEntity);
    }

}
