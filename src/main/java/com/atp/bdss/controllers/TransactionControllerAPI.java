package com.atp.bdss.controllers;

import com.atp.bdss.dtos.requests.create.RequestCreateTransaction;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationTransaction;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import com.atp.bdss.services.ITransactionService;
import com.atp.bdss.utils.Constants;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = Constants.REQUEST_MAPPING_PREFIX + Constants.VERSION_API_V1 + "/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionControllerAPI {

    final ITransactionService transactionService;

    // list transaction with pagination
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDataWithPagination allTransaction(
            @RequestParam(name = "pageIndex", defaultValue = "0") Short pageIndex,
            @RequestParam(name = "pageSize", defaultValue = "10") Short pageSize,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "searchCode", required = false) String searchCode,
            @RequestParam(name = "status", required = false) Short status
    ) {
        RequestPaginationTransaction requestParam = new RequestPaginationTransaction();
        requestParam.setPageIndex(pageIndex);
        requestParam.setPageSize(pageSize);

        if (search != null)
            requestParam.setSearch(search);
        if (status != null)
            requestParam.setStatus(status);
        if (searchCode != null) {
            requestParam.setSearchCode(searchCode);
        }

        return transactionService.allTransactionWithPagination(requestParam);
    }

    //create transaction when user buy land
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData createTransaction(@Valid @RequestBody RequestCreateTransaction request) {

        return transactionService.createTransaction(request);
    }

    // update transaction status
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/confirm-transaction-success-or-fail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData confirmTransactionSuccess(
            @RequestParam("id") String id,
            @RequestParam("status") Short status)
    {

        return transactionService.confirmTransactionSuccessOrFail(id, status);
    }


    // find transaction by id
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getTransactionById(@PathVariable String id){

        return transactionService.findTransactionById(id);
    }


    //list transaction with user id
    @GetMapping(value = "/with-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getTransactionByUserId(@RequestParam("id") String id,
                                               @RequestParam(name = "status", required = false) Short status
    ) {

        return transactionService.findTransactionByUserId(id, status);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData deleteTransactionById(@PathVariable String id){

        return transactionService.deleteTransaction(id);
    }
}
