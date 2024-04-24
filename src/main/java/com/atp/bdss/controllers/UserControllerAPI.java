package com.atp.bdss.controllers;

import com.atp.bdss.dtos.UserInfoFromGoogle;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.services.impl.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserControllerAPI {

    final AccountService accountService;

    @PostMapping("")
    public ResponseData login(@RequestBody UserInfoFromGoogle userInfo){

        return accountService.checkUserInformation(userInfo);
    }


}

