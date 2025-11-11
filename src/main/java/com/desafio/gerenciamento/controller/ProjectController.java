package com.desafio.gerenciamento.controller;

import com.desafio.gerenciamento.model.Project;
import com.desafio.gerenciamento.request.ProjectRequestDTO;
import com.desafio.gerenciamento.response.ProjectResponseDTO;
import com.desafio.gerenciamento.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> criarNovoProjeto(@Valid @RequestBody ProjectRequestDTO request) {
        Project novoProjeto = projectService.criarProjeto(request);
        ProjectResponseDTO response = new ProjectResponseDTO(novoProjeto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
