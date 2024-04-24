package com.atp.bdss.repositories;

import com.atp.bdss.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepositoryJPA extends JpaRepository<Role, Short> {

    Optional<Role> findByName(String user);

}
