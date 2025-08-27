package org.pragma.creditya.r2dbc.persistence.customer;

import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.gateways.CustomerRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CustomerRepositoryAdapterBackup implements CustomerRepository {

    private final CustomerReactiveRepository repository;

    public CustomerRepositoryAdapterBackup(CustomerReactiveRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Customer> save(Customer customer) {
        return repository.save(CustomerMapper.toEntity(customer))
                .map(CustomerMapper::toDomain);
    }
}
