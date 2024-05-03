package com.atp.bdss.services.impl;

import com.atp.bdss.dtos.*;
import com.atp.bdss.dtos.requests.create.RequestCreateTransaction;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationTransaction;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.entities.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

        LandDTO landDTO = returnLandDTO(landRepository, transaction.getLandId());
        transactionDTO.setLand(landDTO);

        Project project = land.getArea().getProject();
        ProjectDTO projectDTO = ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .qrImg(project.getQrImg())
                .bankNumber(project.getBankNumber())
                .bankName(project.getBankName())
                .hostBank(project.getHostBank())
                .build();
        transactionDTO.setProjectDTO(projectDTO);

        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(transactionDTO)
                .build();
    }

    @Override
    public ResponseData createTransaction(RequestCreateTransaction request) {
        // Kiểm tra sự tồn tại của người dùng
        Account account = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorsApp.USER_NOT_YET_AUTHENTICATED));

        // kiem tra trang thai cua nguoi dung
        if (account.getIsDeleted() == Constants.STATUS_ACCOUNT.NOT_YET_AUTHENTICATED)
            throw new CustomException(ErrorsApp.CAN_NOT_BUY_LAND);

        // Kiểm tra sự tồn tại và trạng thái của đất, nếu != STATUS_lAND.IN_PROGRESS thi k duoc
        Land landExisted = landRepository.findById(request.getLandId())
                .orElseThrow(() -> new CustomException(ErrorsApp.LAND_NOT_FOUND));
        if (landExisted.getStatus() != Constants.STATUS_lAND.IN_PROGRESS)
            throw new CustomException(ErrorsApp.CAN_NOT_BUY_LAND);
        // Kiểm tra database transaction, nếu land đã tồn tại trong 1 giao dịch nao day và có trạng thái khác PAYMENT_FAILED thì không được mua
        Optional<Transaction> transactionOptional = transactionRepository.getTransactionByLandId(request.getLandId());

        if (transactionOptional.isPresent()) {
            Transaction landExistedInTransaction = transactionOptional.get();

            if (landExistedInTransaction.getStatus() != Constants.STATUS_TRANSACTION.PAYMENT_FAILED) {
                throw new CustomException(ErrorsApp.CAN_NOT_BUY_LAND);
            }
        }


        Transaction transaction = Transaction.builder()
                .userId(request.getUserId())
                .landId(request.getLandId())
                .status(Constants.STATUS_TRANSACTION.WAIT_FOR_CONFIRMATION)
                .code(generateUniqueRandomString(transactionRepository, 12))
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
        landExisted.setStatus(Constants.STATUS_lAND.LOCKING);
        landRepository.save(landExisted);
        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(transaction)
                .build();
    }

    @Override
    public ResponseData confirmTransactionSuccessOrFail(String id, short status) {
        // // Kiểm tra sự tồn tại của transaction và land
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorsApp.TRANSACTION_NOT_FOUND));
        Land land = landRepository.findById(transaction.getLandId()).orElseThrow(
                () -> new CustomException(ErrorsApp.LAND_NOT_FOUND));

        // Kiểm tra trạng thái từ request có tồn tại không
        if (!findStatusTransaction(status))
            throw new CustomException(ErrorsApp.STATUS_NOT_FOUND);

        // Xác định trạng thái mới cho đất và giao dịch
        if (status == Constants.STATUS_TRANSACTION.PAYMENT_SUCCESS) {
            land.setStatus(Constants.STATUS_lAND.LOCKED);
            transaction.setStatus(Constants.STATUS_TRANSACTION.PAYMENT_SUCCESS);
        } else if (status == Constants.STATUS_TRANSACTION.PAYMENT_FAILED) {
            land.setStatus(Constants.STATUS_lAND.IN_PROGRESS);
            transaction.setStatus(Constants.STATUS_TRANSACTION.PAYMENT_FAILED);
        } else {
            land.setStatus(Constants.STATUS_lAND.LOCKING);
            transaction.setStatus(Constants.STATUS_TRANSACTION.WAIT_FOR_CONFIRMATION);
        }

        landRepository.save(land);
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

    @Override
    public ResponseDataWithPagination allTransactionWithPagination(RequestPaginationTransaction request) {
        Pageable pageable = PageRequest.of(request.getPageIndex() != null ? request.getPageIndex() : 0,
                Math.max(request.getPageSize() != null ? request.getPageSize().intValue() : 10, 1));

        if (request.getSearchByLandName() != null)
            request.setSearchByLandName(request.getSearchByLandName().replace("%", "\\%").replace("_", "\\_").trim());

        Page<Transaction> data = transactionRepository.transactionPagination(request, pageable);

        Page<TransactionDTO> transactions = data.map(transaction -> {
            TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
            UserDTO userDTO = userRepository.findById(transaction.getUserId()).map(
                   user -> modelMapper.map(user, UserDTO.class)
            ).orElseThrow(() -> new CustomException(ErrorsApp.USER_NOT_FOUND));
            transactionDTO.setUser(userDTO);

            LandDTO landDTO = returnLandDTO(landRepository, transaction.getLandId());
            transactionDTO.setLand(landDTO);

            transactionDTO.setLand(landDTO);

            return transactionDTO;
        });

        return ResponseDataWithPagination.builder()
                .currentPage(data.getNumber())
                .currentSize(data.getSize())
                .totalRecords((int) data.getTotalElements())
                .totalPages(data.getTotalPages())
                .totalRecordFiltered(data.getNumberOfElements())
                .data(transactions.getContent())
                .build();
    }

    @Override
    public ResponseData findTransactionByUserId(String userid, Short status) {

        List<TransactionDTO> transactions = transactionRepository.transactionFromUser(userid, status).stream().map(
                transaction -> {
                    TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
                    LandDTO landDTO = returnLandDTO(landRepository, transaction.getLandId());
                    transactionDTO.setLand(landDTO);
                    return transactionDTO;
                }

        ).toList();
        return ResponseData.builder()
                .code(HttpStatus.OK.value())
                .message("Query successfully")
                .data(transactions).build();
    }

    private static LandDTO returnLandDTO(LandRepositoryJPA landRepository, String landId){
        Land land = landRepository.findById(landId)
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
                .typeOfApartment(land.getTypeOfApartment())
                .direction(land.getDirection())
                .build();
        Area area = land.getArea();
        AreaDTO areaDTO = null;
        if (area != null) {
            areaDTO = AreaDTO.builder()
                    .id(area.getId())
                    .name(area.getName())
                    .expiryDate(area.getExpiryDate())
                    .projectId(area.getProject().getId())
                    .projectName(area.getProject().getName())
                    .build();
        }
         landDTO.setAreaDTO(areaDTO);
        return landDTO;
    }

    /*public static String generateUniqueRandomString(TransactionRepositoryJPA transactionRepository) {

        String randomString = null;
        boolean isUnique = false;

        while (!isUnique) {

            randomString = UUID.randomUUID().toString().replaceAll("[^A-Z0-9]", "").substring(0, 12);


            if (!transactionRepository.existsByCode(randomString)) {
                isUnique = true;
            }
        }

        return randomString;
    }*/

    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static SecureRandom random = new SecureRandom();

    public static String generateUniqueRandomString(TransactionRepositoryJPA transactionRepository, int length) {
        StringBuilder sb = new StringBuilder(length);
        boolean isUnique = false;

        while (!isUnique) {
            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
                sb.append(ALLOWED_CHARACTERS.charAt(randomIndex));
            }

            String randomString = sb.toString();

            if (!transactionRepository.existsByCode(randomString)) {
                isUnique = true;
            } else {
                sb.setLength(0); // Reset StringBuilder
            }
        }

        return sb.toString();
    }


}
