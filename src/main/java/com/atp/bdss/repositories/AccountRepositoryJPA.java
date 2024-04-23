package com.atp.bdss.repositories;

import com.atp.bdss.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepositoryJPA extends JpaRepository<Account, String> {
}
