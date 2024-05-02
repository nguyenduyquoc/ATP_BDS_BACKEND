package com.atp.bdss.services.customService;

import com.atp.bdss.dtos.requests.IntrospectRequest;
import com.atp.bdss.dtos.requests.LogoutRequest;
import com.atp.bdss.dtos.requests.RefreshTokenRequest;
import com.atp.bdss.dtos.requests.RequestLogin;
import com.atp.bdss.dtos.responses.IntrospectResponse;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.entities.Account;
import com.atp.bdss.entities.Token;
import com.atp.bdss.exceptions.CustomException;
import com.atp.bdss.repositories.AccountRepositoryJPA;
import com.atp.bdss.repositories.TokenRepositoryJPA;
import com.atp.bdss.utils.ErrorsApp;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationService {

    final AccountRepositoryJPA userRepository;
    final TokenRepositoryJPA tokenRepository;

    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    public ResponseData authentication(RequestLogin request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var user = userRepository
                .findByPhone(request.getPhone())
                .orElseThrow(() -> new CustomException(ErrorsApp.LOGIN_FAILED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) throw new CustomException(ErrorsApp.UNAUTHENTICATED);
        var token = generateToken(user);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Signed in successfully")
                .data(token)
                .build();


    }

    public String generateToken(Account account) {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getPhone())
                .issuer("apt_bds")
                .issueTime(new Date())
                .expirationTime(new Date(
                                Instant.now().plus(1, ChronoUnit.HOURS)
                                        .toEpochMilli()
                        )
                )
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(account))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new CustomException(ErrorsApp.CREATE_TOKEN_FAIL);
        }
    }



    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        Token token = Token
                .builder()
                .id(jit)
                .expiry_date(expiryTime)
                .build();
        tokenRepository.save(token);
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (CustomException e) {
            isValid = false;
        }
        return IntrospectResponse.
                builder().
                valid(isValid)
                .build();

    }

    public ResponseData refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken());

        var jti = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        Token token = Token
                .builder()
                .id(jti)
                .expiry_date(expiryTime)
                .build();
        tokenRepository.save(token);

        var phone = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByPhone(phone).orElseThrow(() -> new CustomException(ErrorsApp.UNAUTHENTICATED));

        var newToken = generateToken(user);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Refresh token successfully")
                .data(newToken)
                .build();
    }



    // get user scope
    private String buildScope(Account user) {
        String scope = "";
        if (user != null && user.getRole() != null) {
            scope = user.getRole().getName();
        }
        return scope;
    }

    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expirationTime != null && expirationTime.before(new Date())) {
            throw new CustomException(ErrorsApp.UNAUTHENTICATED);
        }

        boolean verified = signedJWT.verify(verifier);

        if (!verified)
            throw new CustomException(ErrorsApp.UNAUTHENTICATED);

        // kiem tra token ton tai trong bang token hay k
        if (tokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new CustomException(ErrorsApp.UNAUTHENTICATED);

        return signedJWT;
    }


}
