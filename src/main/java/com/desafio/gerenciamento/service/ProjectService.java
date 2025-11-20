package com.desafio.gerenciamento.service;

import com.desafio.gerenciamento.handler.ProjectExists;
import com.desafio.gerenciamento.handler.ResourceNotFoundException;
import com.desafio.gerenciamento.mapper.ProjectMapper;
import com.desafio.gerenciamento.model.Project;
import com.desafio.gerenciamento.repository.ProjectRepository;
import com.desafio.gerenciamento.request.ProjectRequestDTO;
import com.desafio.gerenciamento.response.ProjectResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository,ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Transactional
    public ProjectResponseDTO criarProjeto(ProjectRequestDTO dto) {
        if(projectRepository.findByName(dto.getName()) != null) {
            throw new ProjectExists("Projeto com o nome " + dto.getName() + " já existe!");
        }
        Project novoProjeto = projectMapper.toEntity(dto);
        Project projetoSalvo = projectRepository.save(novoProjeto);

        return projectMapper.toResponse(projetoSalvo);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> listarProjetos() {
        List<Project> pegarTodos = projectRepository.findAll();
        if(pegarTodos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum projeto encontrado!");
        }
        return projectMapper.toResponseList(pegarTodos);
    }
}

