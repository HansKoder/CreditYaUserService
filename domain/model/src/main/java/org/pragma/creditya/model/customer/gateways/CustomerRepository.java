package org.pragma.creditya.model.customer.gateways;

import org.pragma.creditya.model.customer.Customer;
import reactor.core.publisher.Mono;

public interface CustomerRepository {

    Mono<Customer> save (Customer customer);

}
