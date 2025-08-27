package org.pragma.creditya.usecase.customer;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.gateways.CustomerRepository;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.usecase.customer.command.CreateCustomerCommand;
import org.pragma.creditya.usecase.customer.ports.in.ICustomerUseCase;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CustomerUseCase implements ICustomerUseCase {
    private final CustomerRepository customerRepository;

    @Override
    public Mono<CustomerId> createCustomer(CreateCustomerCommand command) {
        return Mono.just(command)
                .map(cmd -> Customer.create(
                        cmd.name(),
                        cmd.lastName(),
                        cmd.email(),
                        cmd.baseSalary(),
                        cmd.phone()
                ))
                .flatMap(customerRepository::save)
                .map(Customer::getId);
    }
}
