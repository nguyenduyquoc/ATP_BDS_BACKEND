package com.atp.bdss.repositories;

import com.atp.bdss.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepositoryJPA extends JpaRepository<Token, String> {
}
