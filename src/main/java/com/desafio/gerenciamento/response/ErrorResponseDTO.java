package com.desafio.gerenciamento.response;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponseDTO(
    int status,
    String error,
    String message,
    String path,
    List<String> details
)
{
}
