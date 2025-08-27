package org.pragma.creditya.r2dbc.persistence.customer;

import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.valueobject.CustomerId;

import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;


public class CustomerMapperTest {

    private final String CUSTOMER_ID = "2501d95b-e2ee-4c5f-8c1e-72ecdfb24a2b";

    @Test
    void shouldMapperToDomain () {
        CustomerEntity entity = new CustomerEntity();

        entity.setCustomerId(UUID.fromString(CUSTOMER_ID));
        entity.setName("jhon");
        entity.setLastName("doe");
        entity.setEmail("doe@gmail.com");
        entity.setBaseSalary(new BigDecimal(10));
        entity.setPhone(null);

        Customer customer = CustomerMapper.toDomain(entity);

        assertInstanceOf(CustomerId.class, customer.getId());
        assertEquals("2501d95b-e2ee-4c5f-8c1e-72ecdfb24a2b", customer.getId().getValue().toString());
    }

    @Test
    void shouldMapperToEntity () {
        Customer domain = Customer.rebuild(
                UUID.fromString(CUSTOMER_ID),
                "jhon",
                "doe",
                "doe@gmail.com",
                BigDecimal.valueOf(100),
                "310");

        CustomerEntity entity = CustomerMapper.toEntity(domain);

        assertEquals("2501d95b-e2ee-4c5f-8c1e-72ecdfb24a2b", entity.getCustomerId().toString());
    }

}
