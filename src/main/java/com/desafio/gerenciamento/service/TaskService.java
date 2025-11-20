package com.desafio.gerenciamento.service;

import java.util.List;
import java.util.stream.Collectors;

import com.desafio.gerenciamento.mapper.TaskMapper;
import com.desafio.gerenciamento.request.UpdateTaskStatusDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.gerenciamento.handler.ResourceNotFoundException;
import com.desafio.gerenciamento.handler.TaskExists;
import com.desafio.gerenciamento.model.Project;
import com.desafio.gerenciamento.model.Task;
import com.desafio.gerenciamento.repository.ProjectRepository;
import com.desafio.gerenciamento.repository.TaskRepository;
import com.desafio.gerenciamento.request.TaskRequestDTO;
import com.desafio.gerenciamento.response.TaskResponseDTO;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository,TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.taskMapper = taskMapper;
    }

    @Transactional
    public TaskResponseDTO criarTask(TaskRequestDTO dto) {
        if (taskRepository.findByTitle(dto.getTitle()) != null) {
            throw new TaskExists("Essa task já existe!");
        }

        Task novaTask = taskMapper.toEntity(dto);

        Project acharProjeto = projectRepository.findById(dto.getIdProject())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Projeto com ID " + dto.getIdProject() + " não encontrado"));

        novaTask.setProject(acharProjeto);

        Task taskSalva = taskRepository.save(novaTask);

        return taskMapper.toResponse(taskSalva);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> listarTasks(String status, String priority, Long idProject) {
        List<Task> tasks = taskRepository.findByStatusOrPriorityOrIdProject(status, priority, idProject);
        return taskMapper.toResponseList(tasks);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> listarTasks() {
        List<Task> listarTodas = taskRepository.findAll();
        return taskMapper.toResponseList(listarTodas);
    }

    @Transactional
    public TaskResponseDTO atualizarStatus(Long id, UpdateTaskStatusDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task com ID " + id + " não encontrada."));
        task.setStatus(dto.getStatus());
        Task taskAtualizada = taskRepository.save(task);
        return taskMapper.toResponse(taskAtualizada);
    }

    @Transactional
    public void deletarTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task com ID " + id + " não encontrada."));
        taskRepository.delete(task);
    }
}
