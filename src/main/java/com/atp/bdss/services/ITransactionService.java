package com.atp.bdss.services;

import com.atp.bdss.dtos.requests.create.RequestCreateTransaction;
import com.atp.bdss.dtos.requests.pagination.RequestPaginationTransaction;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.dtos.responses.ResponseDataWithPagination;
import org.springframework.stereotype.Service;

@Service
public interface ITransactionService {


    ResponseData findTransactionById(String id);

    ResponseData createTransaction(RequestCreateTransaction request);


    ResponseData confirmTransactionSuccessOrFail(String id, short status);


    ResponseData deleteTransaction(String id);

    ResponseDataWithPagination allTransactionWithPagination(RequestPaginationTransaction request);

    ResponseData findTransactionByUserId(String userid, Short status);
}
