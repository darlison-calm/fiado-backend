package com.fiado.domain.user.repositories;

import com.fiado.domain.phone.PhoneNumberEntity;
import com.fiado.domain.user.entities.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    public boolean existsByEmail(String email);
    public boolean existsByPhoneNumber(PhoneNumberEntity phone);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
}
