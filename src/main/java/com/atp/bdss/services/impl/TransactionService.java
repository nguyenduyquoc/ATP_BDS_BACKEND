package com.atp.bdss.services.impl;

import com.atp.bdss.dtos.LandDTO;
import com.atp.bdss.dtos.TransactionDTO;
import com.atp.bdss.dtos.UserDTO;
import com.atp.bdss.dtos.requests.RequestCreateTransaction;
import com.atp.bdss.dtos.requests.RequestPaginationTransaction;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.entities.Account;
import com.atp.bdss.entities.Land;
import com.atp.bdss.entities.Transaction;
import com.atp.bdss.exceptions.CustomException;
import com.atp.bdss.repositories.AccountRepositoryJPA;
import com.atp.bdss.repositories.LandRepositoryJPA;
import com.atp.bdss.repositories.TransactionRepositoryJPA;
import com.atp.bdss.services.ITransactionService;
import com.atp.bdss.utils.Constants;
import com.atp.bdss.utils.ErrorsApp;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import static com.atp.bdss.utils.CheckerStatus.findStatusTransaction;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public class TransactionService implements ITransactionService {

    final AccountRepositoryJPA userRepository;
    final TransactionRepositoryJPA transactionRepository;
    final LandRepositoryJPA landRepository;
    final ModelMapper modelMapper;

    @Override
    public ResponseDataWithPagination allTransactionWithPagination(RequestPaginationTransaction request) {
        return null;
    }

    @Override
    public ResponseData findTransactionById(String id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorsApp.TRANSACTION_NOT_FOUND));
        TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);

        Account user = userRepository.findById(transaction.getUserId())
                .orElseThrow(() -> new CustomException(ErrorsApp.USER_NOT_FOUND));
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
        transactionDTO.setUser(userDTO);

        Land land = landRepository.findById(transaction.getLandId())
                .orElseThrow(() -> new CustomException(ErrorsApp.LAND_NOT_FOUND));
        LandDTO landDTO = LandDTO.builder()
                .id(land.getId())
                .name(land.getName())
                .description(land.getDescription())
                .thumbnail(land.getThumbnail())
                .status(land.getStatus())
                .address(land.getAddress())
                .price(land.getPrice())
                .deposit(land.getDeposit())
                .acreage(land.getAcreage())
                .build();
        transactionDTO.setLand(landDTO);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(transactionDTO)
                .build();
    }

    @Override
    public ResponseData createTransaction(RequestCreateTransaction request) {

        boolean userExisted = userRepository.existsById(request.getUserId());
        if (!userExisted)
            throw new CustomException(ErrorsApp.USER_NOT_FOUND);

        boolean landExisted = landRepository.existsById(request.getLandId());
        if (!landExisted)
            throw new CustomException(ErrorsApp.LAND_NOT_FOUND);

        Transaction transaction = Transaction.builder()
                .userId(request.getUserId())
                .landId(request.getLandId())
                .status(Constants.STATUS_TRANSACTION.WAIT_FOR_CONFIRMATION)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData updateTransaction(String id, short status) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorsApp.TRANSACTION_NOT_FOUND));
        if (!findStatusTransaction(status))
            throw new CustomException(ErrorsApp.STATUS_NOT_FOUND);

        transaction.setStatus(status);
        transactionRepository.save(transaction);
        return ResponseData
                .builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .build();
    }

    @Override
    public ResponseData deleteTransaction(String id) {


        return null;
    }
}
