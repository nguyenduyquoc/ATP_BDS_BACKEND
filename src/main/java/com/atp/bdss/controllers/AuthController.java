package com.atp.bdss.controllers;

import com.atp.bdss.dtos.requests.RequestLogin;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.services.customService.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthController {

    final AuthenticationService authenticationService;

    @PostMapping("/sign_in")
    public ResponseData login(@RequestBody RequestLogin userLogin){

        return authenticationService.authentication(userLogin);
    }


}
