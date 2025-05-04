package com.fiado.domain.clients;


import com.fiado.domain.clients.dto.ClientRequestDto;
import com.fiado.domain.clients.dto.ClientResponseDto;
import com.fiado.domain.clients.exceptions.ClientNotFoundException;
import com.fiado.domain.clients.mapper.ClientMapper;
import com.fiado.domain.user.entities.UserEntity;
import com.fiado.domain.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Slf4j

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientResponseDto saveClient(ClientRequestDto clientDto, UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + userId));
        ClientEntity clientEntity = clientMapper.toEntity(clientDto);
        clientEntity.setUser(user);
        return clientMapper.toDto(clientRepository.save(clientEntity));
    }

    public List<ClientResponseDto> getAllClientsByUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }
        List<ClientEntity> clientEntities = clientRepository.findAllByUserId(userId);
        return clientEntities.stream().map(clientMapper::toDto).collect(Collectors.toList());
    }

    public ClientResponseDto getClientByIdForUser(Long clientId, UUID userId) {
        ClientEntity client = clientRepository
                .findByIdAndUserId(clientId, userId)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado"));
        return clientMapper.toDto(client);
    }

    @Transactional
    public boolean deleteClientIfBelongsToUser(Long clientId, UUID userId) {
        ClientEntity client = clientRepository
                .findByIdAndUserId(clientId, userId)
                .orElse(null);

        if (client == null) return false;

        clientRepository.delete(client);
        return true;
    }

    public ClientResponseDto updateClientIfBelongsToUser(Long clientId, UUID userId, ClientRequestDto newData) {
        ClientEntity client = clientRepository
                .findByIdAndUserId(clientId, userId)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado"));
        client.setAddress(newData.address());
        client.setFullName(newData.fullName());
        client.setPhoneNumber(newData.phoneNumber());
        client.setObservation(newData.observation());
        ClientEntity updatedClient = clientRepository.save(client);
        return clientMapper.toDto(updatedClient);
    }
}
