package com.desafio.gerenciamento.repository;

import com.desafio.gerenciamento.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    boolean existsByName(String name);
}
