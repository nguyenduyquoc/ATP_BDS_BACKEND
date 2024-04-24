package com.atp.bdss.services.customService;

import com.atp.bdss.configs.TwilioConfig;
import com.atp.bdss.dtos.OtpStatus;
import com.atp.bdss.dtos.requests.RequestOTP;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.entities.Account;
import com.atp.bdss.exceptions.CustomException;
import com.atp.bdss.repositories.AccountRepositoryJPA;
import com.atp.bdss.utils.Constants;
import com.atp.bdss.utils.ErrorsApp;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Random;

@Service
@Slf4j
public class TwilioSmsService {

    @Autowired
    private TwilioConfig twilioConfig;

    private final AccountRepositoryJPA userRepository;

    @Autowired
    public TwilioSmsService(AccountRepositoryJPA userRepository) {
        this.userRepository = userRepository;
    }


    public ResponseData sendOtp(RequestOTP request) {
        Account user = userRepository.findById(request.getUserId()).orElseThrow(
                ()-> new CustomException(ErrorsApp.RECORD_NOT_FOUND)
        );
        ResponseData responseData = null;
        String phone = request.getPhoneNumber();

        try {
            PhoneNumber to = new PhoneNumber(phone);
            PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumber());
            String otp = generateOTP();
            String otpMessage = "Mã otp của bạn là "+ otp;
            Message message = Message.creator(
                            to,
                            from,
                            otpMessage)
                    .create();

            if (phone.startsWith("+84")) {
                phone = "0" + request.getPhoneNumber().substring(3);
            }
            user.setPhone(phone);

            user.setOtp(otp);
            userRepository.save(user);

            responseData = ResponseData.builder()
                    .code(HttpStatus.OK.value())
                    .message(String.valueOf(OtpStatus.DELIVERED))
                    .data(message)
                    .build();

        } catch (Exception e) {
            responseData = ResponseData.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(String.valueOf(OtpStatus.FAILED))
                    .data(e.getMessage())
                    .build();
        }
        return responseData;
    }

    public ResponseData validate(String otp, String userId) {
        Account user = userRepository.findById(userId).orElseThrow(
                ()-> new CustomException(ErrorsApp.RECORD_NOT_FOUND)
        );
        if (!Objects.equals(otp, user.getOtp()))
            throw new CustomException(ErrorsApp.OTP_INCORRECT);

        user.setIsDeleted(Constants.STATUS_ACCOUNT.ACTIVE);
        userRepository.save(user);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Veryfied")
                .build();
    }

    private String generateOTP() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }

}
