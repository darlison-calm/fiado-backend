package com.fiado.domain.authentication;

import com.fiado.domain.user.entities.UserAuthenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
//import java.util.stream.Collectors;

@Service
public class JwtService {
    private final JwtEncoder encoder;

    @Value("${ISSUER}")
    private String ISSUER;

    @Autowired
    public JwtService(JwtEncoder encoder) {
        this.encoder = encoder;
    }
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long JWT_EXPIRE_TIME = 3600L;
        Instant expiry = now.plusSeconds(JWT_EXPIRE_TIME);

        UserAuthenticated user = (UserAuthenticated) authentication.getPrincipal();

//        String scopes = user.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(" "));
        var claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(user.getId().toString())
                .build();
        return  encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
