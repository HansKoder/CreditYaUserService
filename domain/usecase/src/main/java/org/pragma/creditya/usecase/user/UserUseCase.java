package org.pragma.creditya.usecase.user;

import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.user.User;
import org.pragma.creditya.usecase.user.command.CreateUserCommand;
import org.pragma.creditya.usecase.user.ports.in.IUserUseCase;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {

    @Override
    public Mono<User> createUser(CreateUserCommand command) {
        return Mono.fromCallable(() -> User.create(command.username(), command.password()));
    }
}
