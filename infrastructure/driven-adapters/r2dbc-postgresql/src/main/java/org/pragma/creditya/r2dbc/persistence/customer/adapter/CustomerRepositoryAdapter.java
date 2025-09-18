package org.pragma.creditya.r2dbc.persistence.customer.adapter;

import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.gateways.CustomerRepository;
import org.pragma.creditya.r2dbc.helper.ReactiveAdapterOperations;
import org.pragma.creditya.r2dbc.persistence.customer.entity.CustomerEntity;
import org.pragma.creditya.r2dbc.persistence.customer.mapper.CustomCustomerMapper;
import org.pragma.creditya.r2dbc.persistence.customer.repository.CustomerReactiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Repository
public class CustomerRepositoryAdapter extends ReactiveAdapterOperations<
        Customer,
        CustomerEntity,
        UUID,
        CustomerReactiveRepository
        > implements CustomerRepository {
    public CustomerRepositoryAdapter(CustomerReactiveRepository repository, CustomCustomerMapper mapper) {
        super(repository, mapper, mapper::toEntity);
    }

    private final Logger logger = LoggerFactory.getLogger(CustomerRepositoryAdapter.class);

    @Override
    public Mono<Boolean> existByEmail(String email) {
        CustomerEntity probe = new CustomerEntity();
        probe.setEmail(email);

        return repository.exists(Example.of(probe));
    }

    @Override
    public Mono<Boolean> existByDocument(String document) {
        CustomerEntity probe = new CustomerEntity();
        probe.setDocument(document);

        return repository.exists(Example.of(probe));

    }

    @Override
    public Mono<Boolean> checkAllowedRequestLoan(String email, String doc) {
        logger.info("[infra.r2dbc] (checkAllowedRequestLoan) payload [ email:{}, doc:{} ]", email, doc);
        CustomerEntity probe = new CustomerEntity();
        probe.setEmail(email);
        probe.setDocument(doc);

        return repository.exists(Example.of(probe))
                .doOnSuccess(d -> logger.info("[infra.r2dbc] (checkAllowedRequestLoan) find One, payload=[ data:{} ]", d));
    }

    @Override
    public Mono<Customer> getCustomerByDocument(String doc) {
        logger.info("[infra.r2dbc] (getCustomerByDocument) payload [ doc:{} ]", doc);
        CustomerEntity probe = new CustomerEntity();
        probe.setDocument(doc);

        return repository.findOne(Example.of(probe))
                .doOnSuccess(d -> logger.info("[infra.r2dbc] (getCustomerByDocument) find One, payload=[ data:{} ]", d))
                .map(this::toEntity);
    }

    public Mono<Customer> customerAllowedRequestLoan(String email, String doc) {
        logger.info("[infra.r2dbc] (customerAllowedRequestLoan) payload [ email:{}, doc:{} ]", email, doc);
        CustomerEntity probe = new CustomerEntity();
        probe.setEmail(email);
        probe.setDocument(doc);

        return repository.findOne(Example.of(probe))
                .doOnSuccess(d -> logger.info("[infra.r2dbc] (customerAllowedRequestLoan) find One, payload=[ data:{} ]", d))
                .map(this::toEntity);
    }
}
