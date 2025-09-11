package org.pragma.creditya.api.mapper;

import org.pragma.creditya.usecase.customer.query.VerifyOwnershipCustomerQuery;

public class QueryMapper {

    public static VerifyOwnershipCustomerQuery toQuery (String document, String email) {
        return new VerifyOwnershipCustomerQuery(document, email);
    }

}
