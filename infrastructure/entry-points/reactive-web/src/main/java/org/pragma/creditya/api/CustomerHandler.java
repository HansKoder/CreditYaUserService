package org.pragma.creditya.api;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.api.dto.request.CreateCustomerRequest;
import org.pragma.creditya.api.exception.InfrastructureException;
import org.pragma.creditya.api.mapper.CustomerMapper;
import org.pragma.creditya.api.mapper.QueryMapper;
import org.pragma.creditya.usecase.customer.ICustomerUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {

    private final ICustomerUseCase customerUseCase;

    private final Logger logger = LoggerFactory.getLogger(CustomerHandler.class);

    public Mono<ServerResponse> createCustomer(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateCustomerRequest.class)
                .map(CustomerMapper::toCommand)
                .flatMap(customerUseCase::createCustomer)
                .map(CustomerMapper::toResponse)
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED).bodyValue(response))
                .log();
    }

    public Mono<ServerResponse> existCustomerByDocument (ServerRequest serverRequest) {
        return Mono.justOrEmpty(serverRequest.queryParam("document"))
                .switchIfEmpty(Mono.error(new InfrastructureException("Document must be mandatory")))
                .map(CustomerMapper::toQuery)
                .flatMap(customerUseCase::queryCustomerExistByDocument)
                .map(CustomerMapper::toResponse)
                .flatMap(response -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(response))
                .log();
    }

    public Mono<ServerResponse> verifyOwnershipCustomer (ServerRequest serverRequest) {
        logger.info("[infra.reactive-web] (handler) (verifyOwnershipCustomer) 01 - verify ownership customer to request loans");

        String doc = serverRequest.queryParam("document").orElse(null);
        String email = serverRequest.queryParam("email").orElse(null);

        return  Mono.fromCallable(() -> QueryMapper.toQuery(doc, email))
                .doOnSuccess(q -> logger.info("[infra.reactive-web] (handler) (verifyOwnershipCustomer) payload=[ query:{} ]", q))
                .flatMap(customerUseCase::checkCustomerIsAllowedLoan)
                .map(CustomerMapper::verifyToResponse)
                .doOnSuccess(response -> logger.info("[infra-reactive-web] (handler) (verify) request was executed with successful, response=[ response:{} ]", response))
                .doOnError(err -> logger.info("[infra-reactive-web] (handler) (verify) request with unexpected error, response=[ error:{} ]", err.getMessage()))
                .flatMap(response -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(response))
                .log();
    }

    public Mono<ServerResponse> getCustomerByDocument (ServerRequest serverRequest) {
        logger.info("[infra.reactive-web] (handler) (getCustomerByDocument) 01");

        String doc = serverRequest.queryParam("document").orElse(null);
        if (doc == null) return ServerResponse.badRequest().bodyValue("Bad Request");

        return customerUseCase.getCustomerByDocument(doc)
                .map(CustomerMapper::toResponse)
                .flatMap(response -> ServerResponse.status(HttpStatus.OK).bodyValue(response))
                .log();
    }

}
