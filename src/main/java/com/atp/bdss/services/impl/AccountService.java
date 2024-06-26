package com.atp.bdss.services.impl;

import com.atp.bdss.dtos.*;
import com.atp.bdss.dtos.requests.RegisterRequest;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationUser;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    final PasswordEncoder passwordEncoder;


    @Override
    public ResponseData checkUserInformation(UserInfoFromGoogle userInfo) {
        Optional<Account> userOptional = accountRepository.findByEmail(userInfo.getEmail());
        if (userOptional.isEmpty()){
            Role user_role = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));

            // create new account
            Account account = Account.builder()
                    .name(userInfo.getName())
                    .email(userInfo.getEmail())
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


    public ResponseData createAdmin(RegisterRequest request) {
        //check if email existed
        if(accountRepository.existsByEmail(request.getEmail()))
            throw new CustomException(ErrorsApp.EMAIL_EXISTED);
        //check if phone number existed
        if(accountRepository.existsByPhone(request.getPhone()))
            throw new CustomException(ErrorsApp.PHONE_NUMBER_EXISTED);

        Account admin = Account.builder()
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .isDeleted(Constants.ENTITY_STATUS.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();



        // Find role USER, if not create one
        Role admin_role = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));
        admin.setRole(admin_role);

        // save into database
        accountRepository.save(admin);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Created admin successfully")
                .build();
    }

    @Override
    public ResponseData getMyInformation() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Account user = accountRepository.findByPhone(name).orElseThrow(
                () -> new CustomException(ErrorsApp.RECORD_NOT_FOUND)
        );
        UserDTO data = modelMapper.map(user, UserDTO.class);

        data.setRole(user.getRole().getName());

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(data)
                .build();
    }
}
