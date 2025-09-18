package org.pragma.creditya.usecase.customer;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.exception.*;
import org.pragma.creditya.model.customer.gateways.CustomerRepository;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.usecase.customer.command.CreateCustomerCommand;
import org.pragma.creditya.usecase.customer.query.VerifyOwnershipCustomerQuery;
import org.pragma.creditya.usecase.customer.query.ExistDocumentQuery;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class CustomerUseCase implements ICustomerUseCase {
    private final CustomerRepository customerRepository;

    @Override
    public Mono<CustomerId> createCustomer(CreateCustomerCommand command) {
        return Mono.just(command)
                .map(this::checkCustomer)
                .flatMap(this::checkEmail)
                .flatMap(this::checkDocument)
                .flatMap(customerRepository::save)
                .map(Customer::getId);
    }

    @Override
    public Mono<Boolean> queryCustomerExistByDocument(ExistDocumentQuery query) {
        return Mono.just(query)
                .map(q -> checkParamDocument(q.document()))
                .flatMap(customerRepository::existByDocument);
    }

    @Override
    public Mono<Boolean> checkCustomerIsAllowedLoan(VerifyOwnershipCustomerQuery query) {
        return customerRepository.checkAllowedRequestLoan(query.email(), query.document());
    }

    @Override
    public Mono<Customer> getCustomerByDocument(String document) {
        return Mono.fromCallable(() -> checkParamDocument(document))
                .flatMap(customerRepository::getCustomerByDocument)
                .switchIfEmpty(Mono.error(new CustomerDomainException("Customer " + document + " not found")));
    }

    private String checkParamDocument (String document) {
        if (document == null ||  document.isBlank())
            throw new CustomerDomainException("Document is missed");

        return document;
    }

    private Customer checkCustomer (CreateCustomerCommand cmd) {
        return Customer.create(cmd.name(), cmd.lastName(), cmd.email(), cmd.baseSalary(), cmd.document(), cmd.phone());
    }

    private Mono<Customer> checkEmail (Customer customer) {
        return customerRepository.existByEmail(customer.getEmail().value())
                .flatMap(exist -> {
                    if (exist)
                        return Mono.error(new EmailUsedByOtherUserException(customer.getEmail().value()));

                    return Mono.just(customer);
                });
    }

    private Mono<Customer> checkDocument (Customer customer) {
        return customerRepository.existByDocument(customer.getDocument().value())
                .flatMap(exist -> {
                    if (exist)
                        return Mono.error(new DocumentIsUsedByOtherCustomerException(customer.getDocument().value()));

                    return Mono.just(customer);
                });
    }
}
