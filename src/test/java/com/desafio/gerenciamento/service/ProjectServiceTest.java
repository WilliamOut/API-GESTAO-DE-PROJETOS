package com.desafio.gerenciamento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.desafio.gerenciamento.handler.ProjectExists;
import com.desafio.gerenciamento.handler.ResourceNotFoundException;
import com.desafio.gerenciamento.model.Project;
import com.desafio.gerenciamento.repository.ProjectRepository;
import com.desafio.gerenciamento.request.ProjectRequestDTO;
import com.desafio.gerenciamento.response.ProjectResponseDTO;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks // * criar uma instancia da nossa classe de service, injetando os mocks, como o
                 // repository
    private ProjectService projectService;

    @Test
    public void criarProjeto_RetornaProjeto() {
        // arrange
        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("BBB1");
        dto.setDescription("slalalslsls");
        dto.setStartDate(LocalDate.parse("2020-03-21"));
        dto.setEndDate(LocalDate.parse("2024-03-21"));
        Project projeto = new Project(dto.getName(), dto.getDescription(), dto.getStartDate(), dto.getEndDate());

        when(projectRepository.findByName(dto.getName())).thenReturn(null);
        when(projectRepository.save(any(Project.class))).thenReturn(projeto);

        // act
        Project resultado = projectService.criarProjeto(dto);

        // assert
        assertEquals(dto.getName(), resultado.getName());
        assertEquals(dto.getDescription(), resultado.getDescription());
        assertEquals(dto.getStartDate(), resultado.getStartDate());
        assertEquals(dto.getEndDate(), resultado.getEndDate());
    }

    @Test
    public void criarProjeto_LancaExcecaoNomeExistente() {
        // arrange
        String nomeProjeto = "BBB1";
        ProjectRequestDTO dtoComNomeExistente = new ProjectRequestDTO();
        dtoComNomeExistente.setName(nomeProjeto);
        dtoComNomeExistente.setDescription("slalalslsls");
        dtoComNomeExistente.setStartDate(LocalDate.parse("2020-03-21"));
        dtoComNomeExistente.setEndDate(LocalDate.parse("2024-03-21"));

        Project projetoExistente = new Project(
                nomeProjeto,
                "Alguma descrição",
                LocalDate.now(),
                LocalDate.now());
        when(projectRepository.findByName(nomeProjeto)).thenReturn(projetoExistente);

        // act e assert
        ProjectExists exception = assertThrows(ProjectExists.class,
                () -> projectService.criarProjeto(dtoComNomeExistente));
        assertEquals("Projeto com o nome " + nomeProjeto + " já existe!", exception.getMessage());
    }

    @Test
    public void listarProjetos_RetornaListaDeProjetos() {
        // arrange
        Project project1 = new Project("ProjA", "DescA", LocalDate.now(), LocalDate.now().plusMonths(6));
        Project project2 = new Project("ProjB", "DescB", LocalDate.now(), LocalDate.now().plusYears(1));
        List<Project> projetosMock = new ArrayList<>();
        projetosMock.add(project1);
        projetosMock.add(project2);

        when(projectRepository.findAll()).thenReturn(projetosMock);

        // act
        List<ProjectResponseDTO> resultado = projectService.listarProjetos();

        // assert
        assertFalse(resultado.isEmpty(), "A lista de projetos não deve ser vazia");
        assertEquals(2, resultado.size(), "O tamanho da lista deve ser 2");
        assertEquals("ProjA", resultado.get(0).getName());
        assertEquals("ProjB", resultado.get(1).getName());
    }

    @Test
    public void listarProjetos_LancaExcecaoQuandoNaoEncontraProjetos() {
        // arrange
        List<Project> listaVazia = Collections.emptyList();
        when(projectRepository.findAll()).thenReturn(listaVazia);

        // act e assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> projectService.listarProjetos());

        assertEquals("Nenhum projeto encontrado!", ex.getMessage());
    }

}