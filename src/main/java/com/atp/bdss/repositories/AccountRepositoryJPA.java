package com.atp.bdss.repositories;

import com.atp.bdss.dtos.requests.RequestPaginationUser;
import com.atp.bdss.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepositoryJPA extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);

    @Query(value = "select distinct new com.atp.bdss.entities.Account( " +
            "   a.id, " +
            "   a.name, " +
            "   a.email, " +
            "   a.phone " +
            ")" +
            "FROM Account a " +
            "WHERE ((:#{#request.search} IS NULL OR :#{#request.search} = '') " +
            "OR (a.name LIKE %:#{#request.search}% " +
            "OR a.email LIKE %:#{#request.search}% " +
            "OR a.phone LIKE %:#{#request.search}%)) " +
            "AND a.role.id = 2 " +
            " ORDER BY CASE " +
            "            WHEN a.isDeleted = 1 THEN 0 " + // Ưu tiên các dự án có status là 1 lên trên cùng
            "            WHEN a.isDeleted = 2 THEN 1 " + // Sau đó đến các dự án sắp diên ra (status = 0)
            "            ELSE 0 " + // Cuối cùng là các dự án đã kết thúc (các giá trị status khác)
            "          " +
            "          END, " +
            "          a.id DESC")
    Page<Account> getUserPagination(@Param("request") RequestPaginationUser request, Pageable pageable);


}
