package org.pragma.creditya.usecase.customer;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.exception.EmailUsedByOtherUserException;
import org.pragma.creditya.model.customer.gateways.CustomerRepository;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.usecase.customer.command.CreateCustomerCommand;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CustomerUseCase implements ICustomerUseCase {
    private final CustomerRepository customerRepository;

    @Override
    public Mono<CustomerId> createCustomer(CreateCustomerCommand command) {
        return Mono.just(command)
                .map(this::checkCustomer)
                .flatMap(this::checkEmail)
                .flatMap(customerRepository::save)
                .log("Producer consumer ...")
                .map(Customer::getId);
    }

    private Customer checkCustomer (CreateCustomerCommand cmd) {
        return Customer.create(cmd.name(), cmd.lastName(), cmd.email(), cmd.baseSalary(), cmd.phone(), cmd.document());
    }

    private Mono<Customer> checkEmail (Customer customer) {
        return customerRepository.exitsByeEmail(customer.getEmail().value())
                .flatMap(exist -> {
                    if (exist)
                        return Mono.error(new EmailUsedByOtherUserException(customer.getEmail().value()));

                    return Mono.just(customer);
                });
    }
}
