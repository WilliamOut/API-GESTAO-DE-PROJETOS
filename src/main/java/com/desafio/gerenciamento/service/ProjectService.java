package com.desafio.gerenciamento.service;

import com.desafio.gerenciamento.handler.ProjectExists;
import com.desafio.gerenciamento.model.Project;
import com.desafio.gerenciamento.repository.ProjectRepository;
import com.desafio.gerenciamento.request.ProjectRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project criarProjeto(ProjectRequestDTO dto) {
        if(projectRepository.existsByName(dto.getName())) {
            throw new ProjectExists("Projeto com o nome " + dto.getName() + " já existe!");
        }
        Project novoProjeto = new Project();
        novoProjeto.setName(dto.getName());
        novoProjeto.setDescription(dto.getDescription());
        novoProjeto.setStartDate(dto.getStartDate());
        novoProjeto.setEndDate(dto.getEndDate());
        return projectRepository.save(novoProjeto);
    }
}

