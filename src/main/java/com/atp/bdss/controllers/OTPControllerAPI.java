package com.atp.bdss.controllers;

import com.atp.bdss.dtos.requests.RequestOTP;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.services.customService.TwilioSmsService;
import com.atp.bdss.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = Constants.REQUEST_MAPPING_PREFIX + Constants.VERSION_API_V1 + "/otp")
@Slf4j
public class OTPControllerAPI {

    @Autowired
    private TwilioSmsService twilioSmsService;

    // gui otp de xac thuc nguoi dung
    @PostMapping("/send-otp")
    public ResponseData sendOtp(@RequestBody RequestOTP request) {

        return twilioSmsService.sendOtp(request);
    }

    // api xac thuc otp
    @PostMapping("/validate")
    public ResponseData validate(@RequestParam String otp,
                        @RequestParam String userId) {

        return twilioSmsService.validate(otp, userId);
    }



}