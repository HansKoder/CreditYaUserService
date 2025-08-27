package org.pragma.creditya.r2dbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    CustomerRepositoryAdapter repositoryAdapter;

    @Mock
    CustomerReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustSaveValue() {
        /*
        when(repository.save("test")).thenReturn(Mono.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Mono<Object> result = repositoryAdapter.save("test");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();

         */
    }
}
