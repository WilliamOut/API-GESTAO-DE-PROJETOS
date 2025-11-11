package com.desafio.gerenciamento.repository;

import com.desafio.gerenciamento.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
