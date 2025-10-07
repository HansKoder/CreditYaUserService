package org.pragma.creditya.model.customer.gateways;

import org.pragma.creditya.model.customer.Customer;
import reactor.core.publisher.Mono;

public interface CustomerRepository {

    Mono<Customer> save (Customer customer);
    Mono<Boolean> existByEmail (String email);
    Mono<Boolean> existByDocument (String document);
    Mono<Boolean> checkAllowedRequestLoan (String email, String document);
    Mono<Customer> getCustomerByDocument (String document);

}
