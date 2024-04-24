package com.atp.bdss.services.impl;

import com.atp.bdss.dtos.UserInfoFromGoogle;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.entities.Account;
import com.atp.bdss.entities.Role;
import com.atp.bdss.repositories.AccountRepositoryJPA;
import com.atp.bdss.repositories.RoleRepositoryJPA;
import com.atp.bdss.services.IAccountService;
import com.atp.bdss.utils.Constants;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
}
