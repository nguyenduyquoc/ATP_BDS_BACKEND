package com.atp.bdss.services.impl;

import com.atp.bdss.dtos.*;
import com.atp.bdss.dtos.requests.RequestPaginationUser;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.entities.Account;
import com.atp.bdss.entities.Role;
import com.atp.bdss.exceptions.CustomException;
import com.atp.bdss.repositories.AccountRepositoryJPA;
import com.atp.bdss.repositories.RoleRepositoryJPA;
import com.atp.bdss.services.IAccountService;
import com.atp.bdss.utils.Constants;
import com.atp.bdss.utils.ErrorsApp;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public class AccountService implements IAccountService {

    final AccountRepositoryJPA accountRepository;
    final RoleRepositoryJPA roleRepository;
    final ModelMapper modelMapper;


    @Override
    public ResponseData checkUserInformation(UserInfoFromGoogle userInfo) {
        Optional<Account> userOptional = accountRepository.findByEmail(userInfo.getEmail());
        if (userOptional.isEmpty()){
            Role user_role = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));

            // create new account
            Account account = Account.builder()
                    .name(userInfo.getName())
                    .email(userInfo.getName())
                    .createdAt(LocalDateTime.now())
                    .role(user_role)
                    .isDeleted(Constants.STATUS_ACCOUNT.NOT_YET_AUTHENTICATED)
                    .build();
            accountRepository.save(account);
            return ResponseData.builder()
                    .code(HttpStatus.OK.value())
                    .message("NEW")
                    .data(accountRepository.save(account))
                    .build();
        } else {
            // if user existed , update information
            Account account = userOptional.get();
            account.setName(userInfo.getName());
            // Cập nhật các thông tin khác nếu cần thiết
            accountRepository.save(account);
            return ResponseData.builder()
                    .code(HttpStatus.OK.value())
                    .message("OLD")
                    .data(accountRepository.save(account))
                    .build();
        }

    }

    public ResponseDataWithPagination allUserPagination(RequestPaginationUser requestParam) {

        Pageable pageable = PageRequest.of(requestParam.getPageIndex() != null ? requestParam.getPageIndex() : 0,
                Math.max(requestParam.getPageSize() != null ? requestParam.getPageSize().intValue() : 10, 1));

        if (requestParam.getSearch() != null)
            requestParam.setSearch(requestParam.getSearch().replace("%", "\\%").replace("_", "\\_").trim());

        Page<Account> data = accountRepository.getUserPagination(requestParam, pageable);


        Page<UserDTO> users = data.map(user -> modelMapper.map(user, UserDTO.class));

        return ResponseDataWithPagination.builder()
                .currentPage(data.getNumber())
                .currentSize(data.getSize())
                .totalRecords((int) data.getTotalElements())
                .totalPages(data.getTotalPages())
                .totalRecordFiltered(data.getNumberOfElements())
                .data(users.getContent())
                .build();
    }

    @Override
    public ResponseData getInfoUserById(String id) {
        Account user = accountRepository.findById(id).orElseThrow(
                ()-> new CustomException(ErrorsApp.USER_NOT_FOUND)
        );
        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(user)
                .build();

    }
}
