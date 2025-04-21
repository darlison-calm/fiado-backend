package com.fiado.domain.clients;

import com.fiado.domain.authentication.AuthService;
import com.fiado.domain.clients.dto.ClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ClientController {
    private final ClientService clientService;
    private final AuthService authService;

    @GetMapping("users/clients")
    public ResponseEntity<List<ClientDto>> listClientsByUser(Authentication authentication) {
        UUID userId = authService.getUserFromSession(authentication);
        List<ClientDto> clients = clientService.getAllClientsByUser(userId);
        return ResponseEntity.ok(clients);
    }

    @PostMapping("users/clients")
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientEntity client, Authentication authentication) {
        UUID userId = authService.getUserFromSession(authentication);
        ClientDto clientCreated = clientService.saveClient(client, userId);
        return ResponseEntity.ok(clientCreated);
    }

    @DeleteMapping("users/clients/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId, Authentication authentication) {
        UUID userId = authService.getUserFromSession(authentication);
        boolean deleted = clientService.deleteClientIfBelongsToUser(clientId, userId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
