package com.ecotech.plantae.iam.infrastructure.config;

import com.ecotech.plantae.iam.application.handlers.ChangePasswordHandler;
import com.ecotech.plantae.iam.application.handlers.DeleteAccountHandler;
import com.ecotech.plantae.iam.application.handlers.ForgotPasswordHandler;
import com.ecotech.plantae.iam.application.handlers.GetCurrentUserProfileHandler;
import com.ecotech.plantae.iam.application.handlers.GetUserProfileHandler;
import com.ecotech.plantae.iam.application.handlers.LoginUserHandler;
import com.ecotech.plantae.iam.application.handlers.RegisterUserHandler;
import com.ecotech.plantae.iam.application.ports.PasswordHasher;
import com.ecotech.plantae.iam.domain.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamBeansConfig {

    @Bean
    public RegisterUserHandler registerUserHandler(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new RegisterUserHandler(userRepository, passwordHasher);
    }

    @Bean
    public GetUserProfileHandler getUserProfileHandler(UserRepository userRepository) {
        return new GetUserProfileHandler(userRepository);
    }

    @Bean
    public LoginUserHandler loginUserHandler(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new LoginUserHandler(userRepository, passwordHasher);
    }

    @Bean
    public ForgotPasswordHandler forgotPasswordHandler(UserRepository userRepository) {
        return new ForgotPasswordHandler(userRepository);
    }

    @Bean
    public ChangePasswordHandler changePasswordHandler(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new ChangePasswordHandler(userRepository, passwordHasher);
    }

    @Bean
    public DeleteAccountHandler deleteAccountHandler(UserRepository userRepository) {
        return new DeleteAccountHandler(userRepository);
    }

    @Bean
    public GetCurrentUserProfileHandler getCurrentUserProfileHandler(UserRepository userRepository) {
        return new GetCurrentUserProfileHandler(userRepository);
    }
}
