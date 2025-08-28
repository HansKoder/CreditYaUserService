package org.pragma.creditya.r2dbc.persistence.customer.adapter;

import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.gateways.CustomerRepository;
import org.pragma.creditya.r2dbc.helper.ReactiveAdapterOperations;
import org.pragma.creditya.r2dbc.persistence.customer.entity.CustomerEntity;
import org.pragma.creditya.r2dbc.persistence.customer.mapper.CustomCustomerMapper;
import org.pragma.creditya.r2dbc.persistence.customer.repository.CustomerReactiveRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Repository
public class CustomerRepositoryAdapter extends ReactiveAdapterOperations<
        Customer/* change for domain model */,
        CustomerEntity/* change for adapter model */,
        UUID,
        CustomerReactiveRepository
        > implements CustomerRepository {
    public CustomerRepositoryAdapter(CustomerReactiveRepository repository, CustomCustomerMapper mapper) {
        super(repository, mapper, mapper::toEntity);
    }

    @Override
    public Mono<Boolean> exitsByeEmail(String email) {
        CustomerEntity probe = new CustomerEntity();
        probe.setEmail(email);

        return repository.exists(Example.of(probe));
    }
}
