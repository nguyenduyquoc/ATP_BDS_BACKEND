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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthenticationService {

    final AccountRepositoryJPA userRepository;
    final TokenRepositoryJPA tokenRepository;

    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @Value("${jwt.refreshable-duration}")
    protected long REFRESH_DURATION;

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


    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try{
            // verify token
            // Tức là thời gian tồn tại của token đã hết nhưng thời gian refresh thì vẫn có thể còn
            // Vậy nên nếu thời gian refresh vẫn còn thì vẫn phải lưu token đó vào database
            var signToken = verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            Token token = Token
                    .builder()
                    .id(jit)
                    .expiry_date(expiryTime)
                    .build();
            tokenRepository.save(token);

        } catch (CustomException exception) {
            log.info("token already expired");
        }

    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (CustomException e) {
            isValid = false;
        }
        return IntrospectResponse.
                builder().
                valid(isValid)
                .build();

    }

    public ResponseData refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken(), true);

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



    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        // Tạo mới một đối tượng JWSVerifier
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // Token được parse thành một đối tượng SignedJWT
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Xác định thời gian hết hạn của token
        Date expirationTime = (isRefresh) ?
                // refresh token : thời gian phát hành + REFRESH_DURATION
                new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                        .toInstant().plus(REFRESH_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                :
                // authentication or introspect : // thời gian hết hạn
                signedJWT.getJWTClaimsSet().getExpirationTime();

        // Kiểm tra thời gian hết hạn của token
        if (expirationTime != null && expirationTime.before(new Date())) {
            throw new CustomException(ErrorsApp.UNAUTHENTICATED);
        }

        // Xác minh token
        boolean verified = signedJWT.verify(verifier);

        if (!verified)
            throw new CustomException(ErrorsApp.UNAUTHENTICATED);

        // Kiểm tra token có tồn tại trong token table chưa
        if (tokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new CustomException(ErrorsApp.UNAUTHENTICATED);

        return signedJWT;
    }

    private String generateToken(Account account) {

        // create header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // create payload
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getPhone())
                .issuer("apt_bds")
                .issueTime(new Date())
                .expirationTime(new Date(
                                Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS)
                                        .toEpochMilli()
                        )
                )
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(account))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            // sign token
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new CustomException(ErrorsApp.CREATE_TOKEN_FAIL);
        }
    }

    // get user scope
    private String buildScope(Account user) {
        String scope = "";
        if (user != null && user.getRole() != null) {
            scope = user.getRole().getName();
        }
        return scope;
    }


}
