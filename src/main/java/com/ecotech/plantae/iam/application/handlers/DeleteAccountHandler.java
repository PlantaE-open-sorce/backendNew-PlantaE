package com.ecotech.plantae.iam.application.handlers;

import com.ecotech.plantae.iam.application.commands.DeleteAccountCommand;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;
import com.ecotech.plantae.iam.domain.valueobjects.UserId;

public class DeleteAccountHandler {

    private final UserRepository userRepository;

    public DeleteAccountHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void handle(DeleteAccountCommand command) {
        userRepository.delete(UserId.of(command.userId()));
    }
}
