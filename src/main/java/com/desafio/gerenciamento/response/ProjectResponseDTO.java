package com.desafio.gerenciamento.response;

import com.desafio.gerenciamento.model.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Date;

public class ProjectResponseDTO {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    public ProjectResponseDTO() {}

    public ProjectResponseDTO(Project dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
