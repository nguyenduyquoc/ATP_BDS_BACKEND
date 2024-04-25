package com.atp.bdss.controllers;

import com.atp.bdss.dtos.UserInfoFromGoogle;
import com.atp.bdss.dtos.requests.RequestPaginationUser;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.services.impl.AccountService;
import com.atp.bdss.utils.Constants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.REQUEST_MAPPING_PREFIX + Constants.VERSION_API_V1 + "/users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserControllerAPI {

    final AccountService accountService;

    @PostMapping("/login_user")
    public ResponseData login(@RequestBody UserInfoFromGoogle userInfo){

        return accountService.checkUserInformation(userInfo);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDataWithPagination allProjects(
            @RequestParam(name = "pageIndex", defaultValue = "0") Short pageIndex,
            @RequestParam(name = "pageSize", defaultValue = "10") Short pageSize,
            @RequestParam(name = "search", required = false) String search
    ) {
        RequestPaginationUser requestParam = new RequestPaginationUser();
        requestParam.setPageIndex(pageIndex);
        requestParam.setPageSize(pageSize);
        requestParam.setSearch(search);
        return accountService.allUserPagination(requestParam);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getInfoUserById(@PathVariable String id) {

        return accountService.getInfoUserById(id);
    }






}

