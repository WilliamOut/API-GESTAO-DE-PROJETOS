package com.desafio.gerenciamento.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
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
    public void criarProjeto_RetornaProjeto() {
        // 1. O DTO de entrada
        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("BBB1");
        dto.setDescription("Descrição do projeto");
        dto.setStartDate(LocalDate.of(2020, 3, 21)); // Preferível usar .of()
        dto.setEndDate(LocalDate.of(2024, 3, 21));

        // 2. O objeto Project que o MAPPER criaria ao converter o DTO
        Project projetoMapeado = new Project();
        projetoMapeado.setName(dto.getName());
        projetoMapeado.setDescription(dto.getDescription());
        projetoMapeado.setStartDate(dto.getStartDate());
        projetoMapeado.setEndDate(dto.getEndDate());

        // 3. O objeto Project que o REPOSITORY retornaria (simulando ID gerado)
        Project projetoSalvo = new Project();
        projetoSalvo.setId(1L); // ID gerado pelo banco
        projetoSalvo.setName(dto.getName());
        projetoSalvo.setDescription(dto.getDescription());
        projetoSalvo.setStartDate(dto.getStartDate());
        projetoSalvo.setEndDate(dto.getEndDate());

        // 4. O DTO de Resposta que o MAPPER retornaria no final
        ProjectResponseDTO responseDTO = new ProjectResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName(dto.getName());
        responseDTO.setDescription(dto.getDescription());
        responseDTO.setStartDate(dto.getStartDate());
        responseDTO.setEndDate(dto.getEndDate());

        // A. Quando buscar pelo nome, retorna null (não existe, pode criar)
        when(projectRepository.findByName(dto.getName())).thenReturn(null);

        // B. [IMPORTANTE] Quando o mapper converter DTO -> Entity, retorne o objeto mapeado
        when(projectMapper.toEntity(dto)).thenReturn(projetoMapeado);

        // C. Quando o repository salvar, retorne o objeto com ID
        when(projectRepository.save(projetoMapeado)).thenReturn(projetoSalvo);

        // D. [IMPORTANTE] Quando o mapper converter Entity Salva -> Response DTO, retorne o response
        when(projectMapper.toResponse(projetoSalvo)).thenReturn(responseDTO);

        // --- ACT (AÇÃO) ---
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
        // --- ARRANGE ---
        // 1. Lista de Entidades (Vem do Banco)
        Project project1 = new Project("ProjA", "DescA", LocalDate.now(), LocalDate.now().plusMonths(6));
        Project project2 = new Project("ProjB", "DescB", LocalDate.now(), LocalDate.now().plusYears(1));
        List<Project> projetosMock = List.of(project1, project2);

        // 2. Lista de DTOs (Vem do Mapper) - O que esperamos receber no final
        ProjectResponseDTO dto1 = new ProjectResponseDTO();
        dto1.setName("ProjA");

        ProjectResponseDTO dto2 = new ProjectResponseDTO();
        dto2.setName("ProjB");

        List<ProjectResponseDTO> dtosMock = List.of(dto1, dto2);

        // --- MOCKING ---
        when(projectRepository.findAll()).thenReturn(projetosMock);

        // [NOVO] Ensinar o mapper a converter a lista de entidades na lista de DTOs
        when(projectMapper.toResponseList(projetosMock)).thenReturn(dtosMock);

        // --- ACT ---
        List<ProjectResponseDTO> resultado = projectService.listarProjetos();

        // --- ASSERT ---
        assertFalse(resultado.isEmpty(), "A lista de projetos não deve ser vazia");
        assertEquals(2, resultado.size(), "O tamanho da lista deve ser 2");
        assertEquals("ProjA", resultado.get(0).getName());
        assertEquals("ProjB", resultado.get(1).getName());

        // Verificar se o mapper foi chamado
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