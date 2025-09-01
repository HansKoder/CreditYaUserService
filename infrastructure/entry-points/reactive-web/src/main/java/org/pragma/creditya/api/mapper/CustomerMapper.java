package org.pragma.creditya.api.mapper;

import org.pragma.creditya.api.dto.request.CreateCustomerRequest;
import org.pragma.creditya.api.dto.response.CustomerIdResponse;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.usecase.customer.command.CreateCustomerCommand;

public class CustomerMapper {

    public static CreateCustomerCommand toCommand (CreateCustomerRequest request) {
        return new CreateCustomerCommand(
                request.name(),
                request.lastName(),
                request.email(),
                request.phone(),
                request.baseSalary(),
                request.document());
    }

    public static CustomerIdResponse toResponse (CustomerId customerId) {
        return new CustomerIdResponse(customerId.getValue().toString());
    }

}
