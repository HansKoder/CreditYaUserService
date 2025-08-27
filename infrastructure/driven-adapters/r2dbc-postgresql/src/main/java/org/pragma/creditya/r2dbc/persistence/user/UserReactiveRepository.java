package org.pragma.creditya.r2dbc.persistence.user;

import org.pragma.creditya.r2dbc.persistence.customer.CustomerEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

// TODO: This file is just an example, you should delete or modify it
public interface UserReactiveRepository extends ReactiveCrudRepository<CustomerEntity, UUID>, ReactiveQueryByExampleExecutor<CustomerEntity> {

}
