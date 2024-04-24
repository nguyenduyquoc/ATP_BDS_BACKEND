package com.atp.bdss.services.customService;

import com.atp.bdss.dtos.requests.RequestLogin;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.entities.Account;
import com.atp.bdss.repositories.AccountRepositoryJPA;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthenticationService {
    private final JwtEncoder encoder;
    private final AccountRepositoryJPA userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(JwtEncoder encoder, AccountRepositoryJPA userRepository, PasswordEncoder passwordEncoder) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseData authentication(RequestLogin request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email or password incorrect!"));
        boolean auth = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!auth)
            throw new RuntimeException("Email or password incorrect!");
        var token = generateToken(user);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Signed in successfully")
                .data(token)
                .build();


    }

    public String generateToken(Account account) {

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("atp.bds-demo")
                .subject(account.getEmail())
                .issuedAt(new Date().toInstant())
                .expiresAt(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS)
                                .toEpochMilli()
                ).toInstant())
                .claim("scope", buildScope(account))
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String buildScope(Account user) {
        String scope = "";
        if (user != null && user.getRole() != null) {
            scope = user.getRole().getName();
        }
        return scope;
    }

}
