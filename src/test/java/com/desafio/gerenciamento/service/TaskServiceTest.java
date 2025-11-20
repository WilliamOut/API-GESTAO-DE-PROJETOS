package com.desafio.gerenciamento.service;

import com.desafio.gerenciamento.handler.ResourceNotFoundException;
import com.desafio.gerenciamento.handler.TaskExists;
import com.desafio.gerenciamento.mapper.TaskMapper;
import com.desafio.gerenciamento.model.Project;
import com.desafio.gerenciamento.model.Status;
import com.desafio.gerenciamento.model.Task;
import com.desafio.gerenciamento.repository.ProjectRepository;
import com.desafio.gerenciamento.repository.TaskRepository;
import com.desafio.gerenciamento.request.TaskRequestDTO;
import com.desafio.gerenciamento.request.UpdateTaskStatusDTO;
import com.desafio.gerenciamento.response.TaskResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @DisplayName("Deve criar uma task com sucesso vinculada a um projeto")
    public void criarTask_Sucesso() {

        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Nova Task");
        dto.setIdProject(1L);

        Project projetoEncontrado = new Project();
        projetoEncontrado.setId(1L);

        Task taskMapeada = new Task();
        taskMapeada.setTitle("Nova Task");

        Task taskSalva = new Task();
        taskSalva.setId(10L);
        taskSalva.setTitle("Nova Task");
        taskSalva.setProject(projetoEncontrado);

        TaskResponseDTO responseDTO = new TaskResponseDTO();
        responseDTO.setIdTask(10L);
        responseDTO.setIdProject(1L);

        when(taskRepository.findByTitle(dto.getTitle())).thenReturn(null); // Não existe
        when(taskMapper.toEntity(dto)).thenReturn(taskMapeada);
        when(projectRepository.findById(dto.getIdProject())).thenReturn(Optional.of(projetoEncontrado));
        when(taskRepository.save(taskMapeada)).thenReturn(taskSalva);
        when(taskMapper.toResponse(taskSalva)).thenReturn(responseDTO);

        TaskResponseDTO resultado = taskService.criarTask(dto);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getIdTask());
        assertEquals(1L, resultado.getIdProject());

        verify(taskRepository).save(taskMapeada);
        assertEquals(projetoEncontrado, taskMapeada.getProject(), "A task deve ter recebido o projeto antes de salvar");
    }

    @Test
    @DisplayName("Deve lançar exceção se a task já existir pelo título")
    void criarTask_ErroTaskExiste() {
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Task Duplicada");

        when(taskRepository.findByTitle(dto.getTitle())).thenReturn(new Task());

        assertThrows(TaskExists.class, () -> taskService.criarTask(dto));

        verify(taskMapper, never()).toEntity(any());
        verify(projectRepository, never()).findById(any());
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção se o projeto informado não existir")
    void criarTask_ErroProjetoNaoEncontrado() {
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Task Nova");
        dto.setIdProject(99L);

        Task taskMapeada = new Task();

        when(taskRepository.findByTitle(dto.getTitle())).thenReturn(null);
        when(taskMapper.toEntity(dto)).thenReturn(taskMapeada);
        when(projectRepository.findById(dto.getIdProject())).thenReturn(Optional.empty()); // Projeto não achado

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> taskService.criarTask(dto));

        assertEquals("Projeto com ID 99 não encontrado", ex.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar tasks filtradas")
    void listarTasks_ComFiltro() {
        String status = "TODO";
        String priority = "HIGH";
        Long idProject = 1L;

        List<Task> tasksEncontradas = List.of(new Task(), new Task());
        List<TaskResponseDTO> dtosRetornados = List.of(new TaskResponseDTO(), new TaskResponseDTO());

        when(taskRepository.findByStatusOrPriorityOrIdProject(status, priority, idProject))
                .thenReturn(tasksEncontradas);
        when(taskMapper.toResponseList(tasksEncontradas)).thenReturn(dtosRetornados);

        List<TaskResponseDTO> resultado = taskService.listarTasks(status, priority, idProject);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(taskRepository).findByStatusOrPriorityOrIdProject(status, priority, idProject);
    }

    @Test
    @DisplayName("Deve listar todas as tasks")
    void listarTasks_SemFiltro() {
        List<Task> todasTasks = List.of(new Task());
        List<TaskResponseDTO> dtos = List.of(new TaskResponseDTO());

        when(taskRepository.findAll()).thenReturn(todasTasks);
        when(taskMapper.toResponseList(todasTasks)).thenReturn(dtos);

        List<TaskResponseDTO> resultado = taskService.listarTasks();

        assertEquals(1, resultado.size());
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("Deve atualizar o status com sucesso")
    void atualizarStatus_Sucesso() {
        // ARRANGE
        Long id = 1L;
        UpdateTaskStatusDTO dto = new UpdateTaskStatusDTO();
        dto.setStatus(Status.DONE);

        Task taskExistente = new Task();
        taskExistente.setId(id);
        taskExistente.setStatus(Status.TODO);

        Task taskAtualizada = new Task();
        taskAtualizada.setId(id);
        taskAtualizada.setStatus(Status.DONE);

        TaskResponseDTO responseDTO = new TaskResponseDTO();
        responseDTO.setStatus(Status.DONE);

        when(taskRepository.findById(id)).thenReturn(Optional.of(taskExistente));
        when(taskRepository.save(taskExistente)).thenReturn(taskAtualizada);
        when(taskMapper.toResponse(taskAtualizada)).thenReturn(responseDTO);

        TaskResponseDTO resultado = taskService.atualizarStatus(id, dto);

        assertEquals(Status.DONE, resultado.getStatus());
        verify(taskRepository).save(taskExistente);
        assertEquals(Status.DONE, taskExistente.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar status de task inexistente")
    void atualizarStatus_NaoEncontrado() {
        Long id = 99L;
        UpdateTaskStatusDTO dto = new UpdateTaskStatusDTO();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.atualizarStatus(id, dto));
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar task com sucesso")
    void deletarTask_Sucesso() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        taskService.deletarTask(id);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar task inexistente")
    void deletarTask_NaoEncontrado() {
        Long id = 99L;
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.deletarTask(id));
        verify(taskRepository, never()).delete(any());
    }
}