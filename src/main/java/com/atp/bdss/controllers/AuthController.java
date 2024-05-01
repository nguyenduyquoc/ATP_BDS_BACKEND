package com.atp.bdss.controllers;

import com.atp.bdss.dtos.requests.*;
import com.atp.bdss.dtos.responses.IntrospectResponse;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.services.customService.AuthenticationService;
import com.atp.bdss.services.impl.AccountService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthController {

    final AuthenticationService authenticationService;
    final AccountService accountService;

    @PostMapping("/sign_in_admin")
    public ResponseData login(@RequestBody RequestLogin userLogin){

        return authenticationService.authentication(userLogin);
    }

    @PostMapping("/check_token")
    public IntrospectResponse checkToken(@RequestBody IntrospectRequest token) throws ParseException, JOSEException {

        return authenticationService.introspect(token);
    }

    @PostMapping(value = "/sign_up_admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData create(@RequestBody @Valid RegisterRequest request){

        return accountService.createAdmin(request);
    }

    @PostMapping(value = "/logout_admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public void logOut(@RequestBody LogoutRequest request) throws ParseException, JOSEException {

        authenticationService.logout(request);

    }

    @PostMapping(value = "/refresh_token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData refreshToken(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {

        return authenticationService.refreshToken(request);

    }
}
