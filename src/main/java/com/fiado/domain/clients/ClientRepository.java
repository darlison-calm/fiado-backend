package com.fiado.domain.clients;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    List<ClientEntity> findAllByUserId(UUID userId);
    Optional<ClientEntity> findByIdAndUserId(Long clientId, UUID userId);
}
