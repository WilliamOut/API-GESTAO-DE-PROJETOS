package com.desafio.gerenciamento.request;


import com.desafio.gerenciamento.model.Status;
import jakarta.validation.constraints.NotNull;

public class UpdateTaskStatusDTO {

    @NotNull(message = "O novo status é obrigatório")
    private Status status;

    public UpdateTaskStatusDTO() {}

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
