package org.pragma.creditya.api;

import org.pragma.creditya.api.dto.response.ErrorResponse;
import org.pragma.creditya.api.exception.InfrastructureException;
import org.pragma.creditya.model.customer.exception.CustomerDomainException;
import org.pragma.creditya.model.customer.exception.DocumentIsUsedByOtherCustomerException;
import org.pragma.creditya.model.customer.exception.EmailUsedByOtherUserException;
import org.pragma.creditya.model.customer.exception.OwnerShipValidationFailedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CustomerRouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(CustomerHandler handler) {
        return route(POST("/api/users"), handler::createCustomer)
                .filter(domainErrorHandlers())
                .filter(infrastructureErrorHandlers())
                .andRoute(GET("/api/users/exists"), handler::existCustomerByDocument)
                .filter(infrastructureErrorHandlers())
                .andRoute(GET("/api/v1/users/verify-ownership-customer"), handler::verifyOwnershipCustomer)
                .filter(verifyOwnershipCustomerHandlerExceptions());
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> domainErrorHandlers() {
        return (request, next) ->
                next.handle(request)
                        .onErrorResume(CustomerDomainException.class, ex ->
                                ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()))
                        )
                        .onErrorResume(EmailUsedByOtherUserException.class, ex ->
                                ServerResponse.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage())))
                        .onErrorResume(DocumentIsUsedByOtherCustomerException.class, ex ->
                                ServerResponse.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage())))
                ;
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> infrastructureErrorHandlers() {
        return (request, next) ->
                next.handle(request)
                        .onErrorResume(InfrastructureException.class, ex ->
                                ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()))
                        )
                        .onErrorResume(Exception.class, ex ->
                                ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()))
                        );
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> verifyOwnershipCustomerHandlerExceptions() {
        return (request, next) ->
                next.handle(request)
                        .onErrorResume(CustomerDomainException.class, ex ->
                                ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()))
                        )
                        .onErrorResume(OwnerShipValidationFailedException.class, ex ->
                                ServerResponse.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage())))
                ;
    }

}
