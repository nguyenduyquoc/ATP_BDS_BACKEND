package com.atp.bdss.repositories;

import com.atp.bdss.dtos.requests.pagination.RequestPaginationUser;
import com.atp.bdss.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepositoryJPA extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Account> findByPhone(String phone);

    @Query(value = "select new com.atp.bdss.entities.Account( " +
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
            "            WHEN a.isDeleted = 1 THEN 0 " + // Cac user co status la 1 se duoc hien thi truoc
            "            WHEN a.isDeleted = 2 THEN 1 " + // Tiep theo la cac user co status la 2, tuc la chua xac thuc
            "            ELSE 0 " + // Cuối cùng là các user da bi xoa
            "          " +
            "          END, " +
            "          a.id DESC")
    Page<Account> getUserPagination(@Param("request") RequestPaginationUser request, Pageable pageable);


}
