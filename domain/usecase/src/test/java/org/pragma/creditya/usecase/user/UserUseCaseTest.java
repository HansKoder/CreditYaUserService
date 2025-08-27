package org.pragma.creditya.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pragma.creditya.model.user.exception.UserDomainException;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

public class UserUseCaseTest {

    private UserUseCase userUseCase;

    @BeforeEach
    void setup () {
        userUseCase = new UserUseCase();
    }

    @Test
    void shouldThrowExceptionWhenUserNameIsNull () {
        CreateUserCommand cmd = new CreateUserCommand(null, "123456");
        StepVerifier.create(userUseCase.createUser(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Username must be mandatory", throwable.getMessage());
                    assertInstanceOf(UserDomainException.class, throwable);
                }).verify();
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsEmpty () {
        CreateUserCommand cmd = new CreateUserCommand("doe@gmail.com", " ");
        StepVerifier.create(userUseCase.createUser(cmd))
                .expectErrorSatisfies(throwable -> {
                    assertEquals("Password must be mandatory", throwable.getMessage());
                    assertInstanceOf(UserDomainException.class, throwable);
                }).verify();
    }



}
