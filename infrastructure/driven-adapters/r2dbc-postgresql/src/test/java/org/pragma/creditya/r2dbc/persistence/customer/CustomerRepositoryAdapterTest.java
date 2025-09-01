package org.pragma.creditya.r2dbc.persistence.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.r2dbc.persistence.customer.adapter.CustomerRepositoryAdapter;
import org.pragma.creditya.r2dbc.persistence.customer.entity.CustomerEntity;
import org.pragma.creditya.r2dbc.persistence.customer.mapper.CustomCustomerMapper;
import org.pragma.creditya.r2dbc.persistence.customer.repository.CustomerReactiveRepository;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryAdapterTest {

    @InjectMocks
    CustomerRepositoryAdapter repositoryAdapter;

    @Mock
    CustomerReactiveRepository repository;

    @Mock
    CustomCustomerMapper mapper;

    @BeforeEach
    void setup () {
        repository = Mockito.mock(CustomerReactiveRepository.class);
        mapper = Mockito.mock(CustomCustomerMapper.class);

        repositoryAdapter = new CustomerRepositoryAdapter(repository, mapper);
    }

    @Test
    void mustSaveValue() {
        CustomerEntity persisted = new CustomerEntity();
        UUID customerId = UUID.fromString("167e533c-29b4-49bb-89fd-b7b45cade7e7");

        Customer expected = Customer.rebuild(
                customerId,
                "jhon",
                "doe",
                "doe@gmail.com",
                BigDecimal.TEN,
                "113",
                ""
        );

        when(repository.save(any())).thenReturn(Mono.just(persisted));
        when(mapper.toData(any())).thenReturn(persisted);
        when(mapper.toEntity(any())).thenReturn(expected);

        StepVerifier.create(repositoryAdapter.save(expected))
                .expectNextMatches(value -> value.equals(expected))
                .verifyComplete();
    }


    @Test
    void shouldReturnTrueWhenThisEmailWasPersisted() {
        when(repository.exists(any(Example.class)))
                .thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(repositoryAdapter.existByEmail("doe@gmail.com"))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void shouldReturnTrueWhenThisDocumentWasPersisted() {
        when(repository.exists(any(Example.class)))
                .thenReturn(Mono.just(Boolean.TRUE));

        StepVerifier.create(repositoryAdapter.existByDocument("113"))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }



}
