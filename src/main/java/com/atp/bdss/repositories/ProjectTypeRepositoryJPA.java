package com.atp.bdss.repositories;

import com.atp.bdss.entities.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTypeRepositoryJPA extends JpaRepository<ProjectType, Short> {
}
