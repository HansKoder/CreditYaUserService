package org.pragma.creditya.usecase.customer;

import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.usecase.customer.command.CreateCustomerCommand;
import org.pragma.creditya.usecase.customer.query.VerifyOwnershipCustomerQuery;
import org.pragma.creditya.usecase.customer.query.ExistDocumentQuery;
import reactor.core.publisher.Mono;

public interface ICustomerUseCase {
    Mono<CustomerId> createCustomer(CreateCustomerCommand command);
    Mono<Boolean> queryCustomerExistByDocument (ExistDocumentQuery query);
    Mono<Boolean> checkCustomerIsAllowedLoan (VerifyOwnershipCustomerQuery query);
    Mono<Customer> getCustomerByDocument (String document);
}
