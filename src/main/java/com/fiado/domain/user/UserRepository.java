package com.fiado.domain.user;

import com.fiado.domain.phone.PhoneNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    public boolean existsByEmail(String email);
    public boolean existsByPhoneNumber(PhoneNumberEntity phone);

}
