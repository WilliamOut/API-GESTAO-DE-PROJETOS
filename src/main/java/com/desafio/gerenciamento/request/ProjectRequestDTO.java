package com.desafio.gerenciamento.request;

import com.desafio.gerenciamento.model.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Date;

public class ProjectRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3,max = 100,message = "Tamanho do nome fora dos limites(3 - 100)")
    private String name;

    @NotBlank(message = "Descrição é obrigatória")
    private String description;

    @NotNull(message = "A data inicial é obrigatória")
    private LocalDate startDate;

    @NotNull(message = "A data final é obrigatória")
    private LocalDate endDate;

    public ProjectRequestDTO() {}

    public ProjectRequestDTO(Project dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
