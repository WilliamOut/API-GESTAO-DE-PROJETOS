package com.desafio.gerenciamento.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import com.desafio.gerenciamento.mapper.ProjectMapper;
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

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks // * criar uma instancia da nossa classe de service, injetando os mocks, como o
                 // repository
    private ProjectService projectService;

    @Test
    void criarProjeto_RetornaProjeto() {
        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("BBB1");
        dto.setDescription("Descrição do projeto");// Preferível usar .of()
        dto.setEndDate(LocalDate.of(2024, 3, 21));

        Project projetoMapeado = new Project();
        projetoMapeado.setName(dto.getName());
        projetoMapeado.setDescription(dto.getDescription());
        projetoMapeado.setStartDate(dto.getStartDate());
        projetoMapeado.setEndDate(dto.getEndDate());

        Project projetoSalvo = new Project();
        projetoSalvo.setId(1L);
        projetoSalvo.setName(dto.getName());
        projetoSalvo.setDescription(dto.getDescription());
        projetoSalvo.setStartDate(dto.getStartDate());
        projetoSalvo.setEndDate(dto.getEndDate());

        ProjectResponseDTO responseDTO = new ProjectResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName(dto.getName());
        responseDTO.setDescription(dto.getDescription());
        responseDTO.setStartDate(dto.getStartDate());
        responseDTO.setEndDate(dto.getEndDate());

        when(projectRepository.findByName(dto.getName())).thenReturn(null);

        when(projectMapper.toEntity(dto)).thenReturn(projetoMapeado);

        when(projectRepository.save(projetoMapeado)).thenReturn(projetoSalvo);

        when(projectMapper.toResponse(projetoSalvo)).thenReturn(responseDTO);

        ProjectResponseDTO resultado = projectService.criarProjeto(dto);

        assertNotNull(resultado);
        assertEquals(dto.getName(), resultado.getName());
        assertEquals(dto.getDescription(), resultado.getDescription());
        assertEquals(dto.getStartDate(), resultado.getStartDate());
        assertEquals(dto.getEndDate(), resultado.getEndDate());
        assertEquals(1L, resultado.getId());

        verify(projectMapper, times(1)).toEntity(dto);
        verify(projectRepository, times(1)).save(projetoMapeado);
        verify(projectMapper, times(1)).toResponse(projetoSalvo);
    }

    @Test
    void criarProjeto_LancaExcecaoNomeExistente() {
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

        ProjectExists exception = assertThrows(ProjectExists.class,
                () -> projectService.criarProjeto(dtoComNomeExistente));
        assertEquals("Projeto com o nome " + nomeProjeto + " já existe!", exception.getMessage());
    }

    @Test
    void listarProjetos_RetornaListaDeProjetos() {
        Project project1 = new Project("ProjA", "DescA", LocalDate.now(), LocalDate.now().plusMonths(6));
        Project project2 = new Project("ProjB", "DescB", LocalDate.now(), LocalDate.now().plusYears(1));
        List<Project> projetosMock = List.of(project1, project2);

        ProjectResponseDTO dto1 = new ProjectResponseDTO();
        dto1.setName("ProjA");

        ProjectResponseDTO dto2 = new ProjectResponseDTO();
        dto2.setName("ProjB");

        List<ProjectResponseDTO> dtosMock = List.of(dto1, dto2);

        when(projectRepository.findAll()).thenReturn(projetosMock);

        when(projectMapper.toResponseList(projetosMock)).thenReturn(dtosMock);

        List<ProjectResponseDTO> resultado = projectService.listarProjetos();

        assertFalse(resultado.isEmpty(), "A lista de projetos não deve ser vazia");
        assertEquals(2, resultado.size(), "O tamanho da lista deve ser 2");
        assertEquals("ProjA", resultado.get(0).getName());
        assertEquals("ProjB", resultado.get(1).getName());
    }
    @Test
    void listarProjetos_LancaExcecaoQuandoNaoEncontraProjetos() {
        List<Project> listaVazia = Collections.emptyList();
        when(projectRepository.findAll()).thenReturn(listaVazia);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> projectService.listarProjetos());

        assertEquals("Nenhum projeto encontrado!", ex.getMessage());
    }

}