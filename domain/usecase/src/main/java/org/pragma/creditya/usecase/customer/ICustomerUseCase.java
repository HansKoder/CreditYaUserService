package org.pragma.creditya.usecase.customer;

import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.usecase.customer.command.CreateCustomerCommand;
import reactor.core.publisher.Mono;

public interface ICustomerUseCase {
    Mono<CustomerId> createCustomer(CreateCustomerCommand command);
}
