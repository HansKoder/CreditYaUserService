package org.pragma.creditya.r2dbc.persistence.user.repository;

import org.pragma.creditya.r2dbc.persistence.customer.entity.CustomerEntity;
import org.pragma.creditya.r2dbc.persistence.user.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

// TODO: This file is just an example, you should delete or modify it
public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, String>, ReactiveQueryByExampleExecutor<UserEntity> {

}
