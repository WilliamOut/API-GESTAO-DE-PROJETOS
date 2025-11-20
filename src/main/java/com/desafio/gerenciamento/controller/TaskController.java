package com.desafio.gerenciamento.controller;

import java.util.List;

import com.desafio.gerenciamento.request.UpdateTaskStatusDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.desafio.gerenciamento.model.Task;
import com.desafio.gerenciamento.request.TaskRequestDTO;
import com.desafio.gerenciamento.response.TaskResponseDTO;
import com.desafio.gerenciamento.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> criarTask(@Valid @RequestBody TaskRequestDTO request) {
        TaskResponseDTO novaTask = taskService.criarTask(request);
        return new ResponseEntity<>(novaTask,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> listarTasks(@Valid @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority, @RequestParam(required = false) Long idProject) {
        List<TaskResponseDTO> tasks = taskService.listarTasks(status, priority, idProject);
        List<TaskResponseDTO> listarTodasTasks = taskService.listarTasks();

        if (tasks.isEmpty()) {
            return new ResponseEntity<>(listarTodasTasks, HttpStatus.OK);
        }
        if (listarTodasTasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> atualizarStatus(@Valid @PathVariable Long id, @RequestBody UpdateTaskStatusDTO request) {
        TaskResponseDTO taskAtualizada = taskService.atualizarStatus(id,request);
        return new ResponseEntity<>(taskAtualizada,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTask(@PathVariable Long id) {
        taskService.deletarTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
