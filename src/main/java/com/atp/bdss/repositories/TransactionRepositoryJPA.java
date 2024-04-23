package com.atp.bdss.repositories;

import com.atp.bdss.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepositoryJPA extends JpaRepository<Transaction, String> {
}
