package org.pragma.creditya;

import org.junit.jupiter.api.*;
import org.pragma.creditya.model.customer.Customer;
import org.pragma.creditya.model.customer.gateways.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Objects;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CustomerRepositoryIntegrationTest {


    @Container
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withInitScript("db/init/schema.sql");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("adapters.r2dbc.host", postgres::getHost);
        registry.add("adapters.r2dbc.username", postgres::getUsername);
        registry.add("adapters.r2dbc.password", postgres::getPassword);
        registry.add("adapters.r2dbc.database", postgres::getDatabaseName);
        registry.add("adapters.r2dbc.port", () -> postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT));
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Autowired
    private CustomerRepository repository;

    @BeforeEach
    void cleanDatabase() {
        try (Connection conn = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword())) {

            try (Statement stmt = conn.createStatement()) {
                int count = stmt.executeUpdate("delete from customers;");
                System.out.println(">>>>>>>>>>>>>>>>>>  Count users " + count);
            }

        } catch (Exception ex) {
            throw new RuntimeException("Error, detail: ", ex);
        }
    }


    @Test
    void shouldBePersistedWithSuccessful() {
        UUID customerId = UUID.fromString("2501d95b-e2ee-4c5f-8c1e-72ecdfb24a2b");
        Customer entity = Customer.rebuild(customerId, "doe", "doe", "doe@gmail.com", BigDecimal.TEN, "113", "");

        StepVerifier.create(repository.save(entity))
                .expectNextMatches(persisted -> !Objects.isNull(persisted)
                        && persisted.getId() != null
                        && persisted.getDocument() != null
                )
                .verifyComplete();
    }


    @Test
    void shouldBePersistedAndFoundByEmail() {
        UUID customerId = UUID.fromString("2501d95b-e2ee-4c5f-8c1e-72ecdfb24a2b");
        Customer entity = Customer.rebuild(customerId, "doe", "doe", "doe@gmail.com", BigDecimal.TEN, "113", "");


        StepVerifier.create(repository.save(entity))
                .expectNextMatches(persisted -> !Objects.isNull(persisted)
                        && persisted.getId() != null
                        && persisted.getDocument() != null
                )
                .verifyComplete();

        StepVerifier.create(repository.existByEmail("doe@gmail.com"))
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void shouldBePersistedButDocumentDoesNotHaveMatch() {
        UUID customerId = UUID.fromString("2501d95b-e2ee-4c5f-8c1e-72ecdfb24a2b");
        Customer entity = Customer.rebuild(customerId, "doe", "doe", "doe@gmail.com", BigDecimal.TEN, "113", "");

        StepVerifier.create(repository.save(entity))
                .expectNextMatches(persisted -> !Objects.isNull(persisted)
                        && persisted.getId() != null
                        && persisted.getDocument() != null
                )
                .verifyComplete();

        StepVerifier.create(repository.existByDocument("102"))
                .expectNext(Boolean.FALSE)
                .verifyComplete();
    }


}
