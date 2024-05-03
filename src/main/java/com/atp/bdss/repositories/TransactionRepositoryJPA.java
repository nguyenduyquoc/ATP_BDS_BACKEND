package com.atp.bdss.repositories;

import com.atp.bdss.dtos.requests.RequestPaginationTransaction;
import com.atp.bdss.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepositoryJPA extends JpaRepository<Transaction, String> {

    Optional<Transaction> getTransactionByLandId(String landId);

    @Query(value = "select distinct new com.atp.bdss.entities.Transaction( " +
            "   t.id, " +
            "   t.userId, " +
            "   t.landId," +
            "   t.status," +
            "   t.createdAt," +
            "   t.updateAt" +
            ")" +
            "from Transaction t " +
            "left join Land l on l.id = t.landId " +
            "left join Account a on l.id = t.userId " +
            "WHERE " +
            "(:#{#request.status} IS NULL OR t.status = :#{#request.status}) " +
            "   AND ((:#{#request.searchByLandName} IS NULL OR :#{#request.searchByLandName} = '') " +
            "       OR (l.name LIKE %:#{#request.searchByLandName}% " +
            "       OR l.area.name LIKE %:#{#request.searchByLandName}% " +
            "       OR l.area.project.name LIKE %:#{#request.searchByLandName}%)) " +
            " ORDER BY CASE " +
            "            WHEN t.status = 0 THEN 0 " + // Ưu tiên các giao dịch chờ được xử lí
            "            WHEN t.status = 1 THEN 1 " + // Sau đó đến các giao dịch đã được xử lí
            "            ELSE 2 " + // Cuối cùng là các giao dịch đã bị hủy
            "          " +
            "          END, " +
            "          t.id DESC")
    Page<Transaction> transactionPagination(@Param("request") RequestPaginationTransaction request, Pageable pageable);


    @Query(value = "select new com.atp.bdss.entities.Transaction( " +
            "   t.id, " +
            "   t.userId, " +
            "   t.landId," +
            "   t.status," +
            "   t.createdAt," +
            "   t.updateAt" +
            ")" +
            "from Transaction t " +
            "left join Land l on l.id = t.landId " +
            "left join Account a on l.id = t.userId " +
            "WHERE " +
            "(:#{#id} IS NULL OR t.userId = :#{#id}) " +
            "and (:#{#status} IS NULL OR t.status = :#{#status}) " +
            " ORDER BY CASE " +
            "            WHEN t.status = 0 THEN 0 " + // Ưu tiên các giao dịch chờ được xử lí
            "            WHEN t.status = 1 THEN 1 " + // Sau đó đến các giao dịch đã được xử lí
            "            ELSE 2 " + // Cuối cùng là các giao dịch đã bị hủy
            "          " +
            "          END, " +
            "          t.id DESC")
    List<Transaction> transactionFromUser(@Param("id") String id, @Param("status") Short status);


    boolean existsByCode(String code);
}
