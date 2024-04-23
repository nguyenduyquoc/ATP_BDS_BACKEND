package com.atp.bdss.controllers;

import com.atp.bdss.dtos.requests.RequestCreateTransaction;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.services.ITransactionService;
import com.atp.bdss.utils.Constants;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.REQUEST_MAPPING_PREFIX + Constants.VERSION_API_V1 + "/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionControllerAPI {

    final ITransactionService transactionService;

    //create transaction when user buy land
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData createTransaction(@Valid @RequestBody RequestCreateTransaction request) {

        return transactionService.createTransaction(request);
    }

    // update transaction status
    @PutMapping(value = "/updateTransactionStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData updateTransactionStatus(
            @RequestParam("id") String id,
            @RequestParam("status") short status)
    {

        return transactionService.updateTransaction(id, status);
    }


    // find transaction by id
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData getProjectById(@PathVariable String id){

        return transactionService.findTransactionById(id);
    }


}
