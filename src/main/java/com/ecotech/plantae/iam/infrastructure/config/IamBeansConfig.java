package com.ecotech.plantae.iam.infrastructure.config;

import com.ecotech.plantae.iam.application.internal.commandhandlers.ChangePasswordHandler;
import com.ecotech.plantae.iam.application.internal.commandhandlers.DeleteAccountHandler;
import com.ecotech.plantae.iam.application.internal.commandhandlers.ForgotPasswordHandler;
import com.ecotech.plantae.iam.application.internal.commandhandlers.LoginUserHandler;
import com.ecotech.plantae.iam.application.internal.commandhandlers.ResetPasswordHandler;
import com.ecotech.plantae.iam.application.internal.commandhandlers.RegisterUserHandler;
import com.ecotech.plantae.iam.application.internal.queryhandlers.GetCurrentUserProfileHandler;
import com.ecotech.plantae.iam.application.internal.queryhandlers.GetUserProfileHandler;
import com.ecotech.plantae.iam.application.internal.outboundservices.PasswordResetNotifier;
import com.ecotech.plantae.iam.application.internal.outboundservices.PasswordHasher;
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
    public ForgotPasswordHandler forgotPasswordHandler(UserRepository userRepository, PasswordResetNotifier passwordResetNotifier) {
        return new ForgotPasswordHandler(userRepository, passwordResetNotifier);
    }

    @Bean
    public ChangePasswordHandler changePasswordHandler(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new ChangePasswordHandler(userRepository, passwordHasher);
    }

    @Bean
    public ResetPasswordHandler resetPasswordHandler(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new ResetPasswordHandler(userRepository, passwordHasher);
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
