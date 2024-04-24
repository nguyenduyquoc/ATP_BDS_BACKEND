package com.atp.bdss.repositories;

import com.atp.bdss.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepositoryJPA extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);
}
