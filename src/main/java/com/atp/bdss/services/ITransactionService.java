package com.atp.bdss.services;

import com.atp.bdss.dtos.requests.RequestCreateTransaction;
import com.atp.bdss.dtos.requests.RequestPaginationTransaction;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import org.springframework.stereotype.Service;

@Service
public interface ITransactionService {

    ResponseDataWithPagination allTransactionWithPagination(RequestPaginationTransaction request);

    ResponseData findTransactionById(String id);

    ResponseData createTransaction(RequestCreateTransaction request);


    ResponseData updateTransaction(String id, short status);

    ResponseData deleteTransaction(String id);
}
