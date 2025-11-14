package com.desafio.gerenciamento.service;

import com.desafio.gerenciamento.handler.ProjectExists;
import com.desafio.gerenciamento.model.Project;
import com.desafio.gerenciamento.repository.ProjectRepository;
import com.desafio.gerenciamento.request.ProjectRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks // * criar uma instancia da nossa classe de service, injetando os mocks, como o repository
    private ProjectService projectService;

    @Nested // * junit identificar que é uma subclasse
    class criarProjeto{

        @Test
        @DisplayName("Deve criar um projeto com sucesso")
        void deveCriarUmProjeto() {
            //dado
            ProjectRequestDTO dto = new ProjectRequestDTO();
            dto.setName("BBBB");
            dto.setDescription("Descrição do projeto");
            dto.setEndDate(LocalDate.now());
            dto.setEndDate(LocalDate.now().plusMonths(3));

            Project novoProjeto = new Project();
            novoProjeto.setName(dto.getName());
            novoProjeto.setDescription(dto.getDescription());
            novoProjeto.setStartDate(dto.getStartDate());
            novoProjeto.setEndDate(dto.getEndDate());


            //quando
            when(projectRepository.findByName(dto.getName())).thenReturn(null);
            when(projectRepository.save(any(Project.class))).thenReturn(novoProjeto);
            Project output = projectService.criarProjeto(dto);


            //entao
            assertNotNull(output);
            assertEquals(output.getId(),novoProjeto.getId());
            assertEquals(dto.getName(),output.getName());
            assertEquals(dto.getStartDate(),output.getStartDate());
            verify(projectRepository,times(1)).findByName(dto.getName());
            verify(projectRepository,times(1)).save(any(Project.class));
        }

        @Test
        @DisplayName("Deve lançar uma exceção se o projeto já existir")
        void deveLancarExcecaoSeNomeForEncontrado() {
            //dado
            ProjectRequestDTO dto = new ProjectRequestDTO();
            dto.setName("BBBB");
            dto.setDescription("Descrição do projeto");
            dto.setEndDate(LocalDate.now());
            dto.setEndDate(LocalDate.now().plusMonths(3));
            Project projetoExistente = new Project(1L,"BBBB","Descrição do projeto",LocalDate.now(),LocalDate.now().plusMonths(3));

            //quando
            when(projectRepository.findByName(dto.getName())).thenReturn(projetoExistente);


            //entao
            assertThrows(ProjectExists.class, () -> {
                projectService.criarProjeto(dto);
            });
            verify(projectRepository,never()).save(any(Project.class));
        }

    }
}