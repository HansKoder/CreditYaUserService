package org.pragma.creditya.usecase.customer;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.exception.CustomerDomainException;
import org.pragma.creditya.model.customer.exception.DocumentIsUsedByOtherCustomerException;
import org.pragma.creditya.model.customer.exception.EmailUsedByOtherUserException;
import org.pragma.creditya.model.customer.exception.OwnerShipValidationFailedException;
import org.pragma.creditya.model.customer.gateways.CustomerRepository;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.usecase.customer.command.CreateCustomerCommand;
import org.pragma.creditya.usecase.customer.query.VerifyOwnershipCustomerQuery;
import org.pragma.creditya.usecase.customer.query.ExistDocumentQuery;
import reactor.core.publisher.Mono;

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
                .map(this::checkParamDocument)
                .flatMap(customerRepository::existByDocument);
    }

    @Override
    public Mono<Customer> checkCustomerIsAllowedLoan(VerifyOwnershipCustomerQuery query) {
        return customerRepository.customerAllowedRequestLoan(query.email(), query.document())
                .switchIfEmpty(Mono.error(new OwnerShipValidationFailedException("Operation Invalid")));
    }

    private String checkParamDocument (ExistDocumentQuery query) {
        if (query == null || query.document() == null || query.document().isBlank())
            throw new CustomerDomainException("Document is missed");

        return query.document();
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
