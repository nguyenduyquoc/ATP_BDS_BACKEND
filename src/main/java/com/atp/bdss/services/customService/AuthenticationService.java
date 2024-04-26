package com.atp.bdss.services.customService;

import com.atp.bdss.dtos.requests.RequestLogin;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.entities.Account;
import com.atp.bdss.exceptions.CustomException;
import com.atp.bdss.repositories.AccountRepositoryJPA;
import com.atp.bdss.utils.ErrorsApp;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthenticationService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final AccountRepositoryJPA userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(JwtEncoder encoder, JwtDecoder decoder, AccountRepositoryJPA userRepository, PasswordEncoder passwordEncoder) {
        this.jwtEncoder = encoder;
        this.jwtDecoder = decoder;
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
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String buildScope(Account user) {
        String scope = "";
        if (user != null && user.getRole() != null) {
            scope = user.getRole().getName();
        }
        return scope;
    }

    /*public String introspect(String request) throws ParseException, JOSEException {
        var token = request;

        verifyToken(token);
        return IntrospectResponse.builder()
                .valid(true)
                .build();

    }*/

    public boolean verifyToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            Instant expiresAt = jwt.getExpiresAt();
            if (expiresAt != null && expiresAt.isBefore(Instant.now())) {
                return false; // Token đã hết hạn
            }

            // Token hợp lệ nếu không có ngoại lệ và không hết hạn
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
