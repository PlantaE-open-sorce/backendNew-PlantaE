package com.ecotech.plantae.shared.application.security;

import com.ecotech.plantae.iam.domain.entities.User;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;
import com.ecotech.plantae.iam.domain.valueobjects.UserAccountType;
import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Component
public class AccountTypeGuard {

    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;

    public AccountTypeGuard(CurrentUserProvider currentUserProvider, UserRepository userRepository) {
        this.currentUserProvider = currentUserProvider;
        this.userRepository = userRepository;
    }

    public UserAccountType require(UserAccountType... allowedTypes) {
        String userId = currentUserProvider.getCurrentUserId()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        User accountOwner = userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        UserAccountType accountType = accountOwner.getAccountType();
        if (allowedTypes != null && allowedTypes.length > 0) {
            boolean allowed = Arrays.stream(allowedTypes).anyMatch(accountType::equals);
            if (!allowed) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account type " + accountType + " is not allowed");
            }
        }
        return accountType;
    }
}
