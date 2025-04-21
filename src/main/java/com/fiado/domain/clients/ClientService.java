package com.fiado.domain.clients;


import com.fiado.domain.clients.dto.ClientDto;
import com.fiado.domain.clients.mapper.ClientMapper;
import com.fiado.domain.user.entities.UserEntity;
import com.fiado.domain.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientDto saveClient(ClientEntity clientEntity, UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + userId));

        clientEntity.setUser(user);
        return clientMapper.toDto(clientRepository.save(clientEntity));
    }

    public List<ClientDto> getAllClientsByUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }
        List<ClientEntity> clientEntities = clientRepository.findAllByUserId(userId);
        return clientEntities.stream().map(clientMapper::toDto).collect(Collectors.toList());
    }

    public Optional<ClientEntity> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public boolean deleteClientIfBelongsToUser(Long clientId, UUID userId) {
        Optional<ClientEntity> client = clientRepository.findByIdAndUserId(clientId, userId);
        if (client.isPresent()) {
            clientRepository.delete(client.get());
            return true;
        }
        return false;
    }

}
