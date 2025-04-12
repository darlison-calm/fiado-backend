package com.fiado.domain.authentication;

import com.fiado.domain.user.entities.UserAuthenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    @Autowired
    public JwtService(JwtEncoder encoder, JwtDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(3600L);

        UserAuthenticated user = (UserAuthenticated) authentication.getPrincipal();

        String scopes = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        var claims = JwtClaimsSet.builder()
                .issuer("fazfiadomano")
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(authentication.getName())
                .claim("id", user.getId())
                .build();
        return  encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
