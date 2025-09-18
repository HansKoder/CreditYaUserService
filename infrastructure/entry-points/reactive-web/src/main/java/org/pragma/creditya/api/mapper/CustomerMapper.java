package org.pragma.creditya.api.mapper;

import org.pragma.creditya.api.dto.request.CreateCustomerRequest;
import org.pragma.creditya.api.dto.response.CustomerIdResponse;
import org.pragma.creditya.api.dto.response.CustomerResponse;
import org.pragma.creditya.api.dto.response.ExistCustomerDocumentResponse;
import org.pragma.creditya.api.dto.response.VerifyCustomerAllowedApplicationResponse;
import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.valueobject.CustomerId;
import org.pragma.creditya.usecase.customer.command.CreateCustomerCommand;
import org.pragma.creditya.usecase.customer.query.ExistDocumentQuery;
import org.pragma.creditya.usecase.customer.query.ExistDocumentQuery;

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

    public static CustomerResponse toResponse (Customer customer) {
        return new CustomerResponse(
                customer.getDocument().value(),
                customer.getEmail().value(),
                customer.getBaseSalary().getAmount(),
                customer.getFullName().name()
        );
    }

    public static ExistDocumentQuery toQuery (String document) {
        return new ExistDocumentQuery(document);
    }

    public static ExistCustomerDocumentResponse toResponse (Boolean exist) {
        return new ExistCustomerDocumentResponse(exist);
    }

    public static VerifyCustomerAllowedApplicationResponse  verifyToResponse (Boolean exits) {
        return new VerifyCustomerAllowedApplicationResponse(exits);
    }

}
