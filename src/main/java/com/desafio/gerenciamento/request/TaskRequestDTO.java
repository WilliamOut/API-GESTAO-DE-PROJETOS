package com.desafio.gerenciamento.request;

import java.time.LocalDate;

import com.desafio.gerenciamento.model.Priority;
import com.desafio.gerenciamento.model.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TaskRequestDTO {

    @NotBlank(message = "O título não pode ser vazio")
    @Size(min = 5, max = 150, message = "O limite do título tem que ser 5-150 caracteres")
    private String title;

    @NotBlank(message = "A descrição não pode ser vazia")
    private String description;

    @NotNull(message = "O status não pode ser nulo")
    private Status status;

    @NotNull(message = "A prioridade não pode ser nulo")
    private Priority priority;

    @NotNull(message = "O dia de entrega não pode ser nulo")
    private LocalDate dueDate;

    @NotNull(message = "O ID do projeto é obrigatório.")
    private Long idProject;

    public TaskRequestDTO() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getIdProject() {
        return idProject;
    }

    public void setIdProject(Long idProject) {
        this.idProject = idProject;
    }

}
