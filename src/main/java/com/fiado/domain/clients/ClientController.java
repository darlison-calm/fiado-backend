package com.fiado.domain.clients;

import com.fiado.domain.authentication.AuthService;
import com.fiado.domain.clients.dto.ClientRequestDto;
import com.fiado.domain.clients.dto.ClientResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/clients")
public class ClientController {
    private final ClientService clientService;
    private final AuthService authService;

    @GetMapping()
    public ResponseEntity<List<ClientResponseDto>> listClientsByUser(Authentication authentication) {
        UUID userId = authService.getUserFromSession(authentication);
        List<ClientResponseDto> clients = clientService.getAllClientsByUser(userId);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<ClientResponseDto> getClientForUser(@PathVariable Long clientId, Authentication authentication) {
        UUID userId = authService.getUserFromSession(authentication);
        ClientResponseDto client = clientService.getClientByIdForUser(clientId, userId);
        return ResponseEntity.ok(client);
    }

    @PostMapping()
    public ResponseEntity<ClientResponseDto> createClient(@RequestBody ClientRequestDto client, Authentication authentication) {
        UUID userId = authService.getUserFromSession(authentication);
        ClientResponseDto clientCreated = clientService.saveClient(client, userId);
        return ResponseEntity.ok(clientCreated);
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId, Authentication authentication) {
        UUID userId = authService.getUserFromSession(authentication);
        boolean deleted = clientService.deleteClientIfBelongsToUser(clientId, userId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientResponseDto> updateClient(@PathVariable Long clientId, Authentication authentication, @RequestBody ClientRequestDto clientDto) {
        UUID userId = authService.getUserFromSession(authentication);
        ClientResponseDto client = clientService.updateClientIfBelongsToUser(clientId, userId, clientDto);
        return ResponseEntity.ok(client);
    }
}
