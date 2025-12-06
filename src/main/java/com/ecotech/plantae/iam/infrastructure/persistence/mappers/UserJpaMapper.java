package com.ecotech.plantae.iam.infrastructure.persistence.mappers;

import com.ecotech.plantae.iam.domain.entities.User;
import com.ecotech.plantae.iam.domain.valueobjects.UserAccountType;
import com.ecotech.plantae.iam.domain.valueobjects.UserId;
import com.ecotech.plantae.iam.domain.valueobjects.UserLanguage;
import com.ecotech.plantae.iam.domain.valueobjects.UserRole;
import com.ecotech.plantae.iam.infrastructure.persistence.UserJpaEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("null")
public class UserJpaMapper {

    public @NonNull UserJpaEntity toJpa(@NonNull User user) {
        return new UserJpaEntity(
                user.getId().value(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getDisplayName(),
                user.getLanguage().name(),
                user.getRole().name(),
                user.getAccountType().name(),
                user.isActive(),
                user.getPasswordResetToken(),
                user.getPasswordResetTokenExpiresAt()
        );
    }

    public @NonNull User toDomain(@NonNull UserJpaEntity entity) {
        return User.restore(
                UserId.of(entity.getId()),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getDisplayName(),
                UserLanguage.from(entity.getLanguage()),
                UserRole.valueOf(entity.getRole()),
                UserAccountType.from(entity.getAccountType()),
                entity.isActive(),
                entity.getPasswordResetToken(),
                entity.getPasswordResetTokenExpiresAt()
        );
    }
}
