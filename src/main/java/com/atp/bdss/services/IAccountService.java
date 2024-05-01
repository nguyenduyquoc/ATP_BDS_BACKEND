package com.atp.bdss.services;

import com.atp.bdss.dtos.UserInfoFromGoogle;
import com.atp.bdss.dtos.requests.RegisterRequest;
import com.atp.bdss.dtos.requests.RequestPaginationUser;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import org.springframework.stereotype.Service;

@Service
public interface IAccountService {

    ResponseData checkUserInformation (UserInfoFromGoogle userInfo);

    public ResponseDataWithPagination allUserPagination(RequestPaginationUser requestParam);

    ResponseData getInfoUserById(String id);

    ResponseData createAdmin(RegisterRequest request);

    ResponseData getMyInformation();
}
