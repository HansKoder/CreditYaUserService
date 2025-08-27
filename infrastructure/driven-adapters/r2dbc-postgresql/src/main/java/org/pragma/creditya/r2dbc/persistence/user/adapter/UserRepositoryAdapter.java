package org.pragma.creditya.r2dbc.persistence.user.adapter;

import org.pragma.creditya.model.user.User;
import org.pragma.creditya.r2dbc.helper.ReactiveAdapterOperations;
import org.pragma.creditya.r2dbc.persistence.user.entity.UserEntity;
import org.pragma.creditya.r2dbc.persistence.user.mapper.UserMapper;
import org.pragma.creditya.r2dbc.persistence.user.repository.UserReactiveRepository;

public class UserRepositoryAdapter extends ReactiveAdapterOperations<
        User/* change for domain model */,
        UserEntity/* change for adapter model */,
        String,
        UserReactiveRepository
        > {
    public UserRepositoryAdapter(UserReactiveRepository repository, UserMapper mapper) {
        super(repository, mapper, mapper::toEntity);
    }
}
