package org.pragma.creditya.r2dbc.mapper;

public interface DomainDataMapper<E, D> {
    D toData(E entity);
    E toDomain(D data);
}