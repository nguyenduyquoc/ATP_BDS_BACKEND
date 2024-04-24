package com.atp.bdss.services;

import com.atp.bdss.dtos.UserInfoFromGoogle;
import com.atp.bdss.dtos.responses.ResponseData;
import org.springframework.stereotype.Service;

@Service
public interface IAccountService {

    ResponseData checkUserInformation (UserInfoFromGoogle userInfo);

}
