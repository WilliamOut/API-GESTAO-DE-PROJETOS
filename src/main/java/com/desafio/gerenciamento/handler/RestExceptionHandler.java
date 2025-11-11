package com.desafio.gerenciamento.handler;

import com.desafio.gerenciamento.response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    // * nome de projeto igual exception
    @ExceptionHandler(ProjectExists.class)
    public ResponseEntity<Object> handleProjectExists(ProjectExists ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        String path = request.getDescription(false).replace("uri=","");

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                path,
                null
        );

        return new ResponseEntity<>(errorResponseDTO, status);
    }

    // * json mal formatado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleJsonErrors(HttpMessageNotReadableException ex,WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String detailMessage = "Corpo da requisição inválido. Verifique a sintaxe JSON!";
        String path = request.getDescription(false).replace("uri=","");

        if(ex.getRootCause() != null) {
            detailMessage = ex.getRootCause().getMessage();
        }
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                status.value(),
                status.getReasonPhrase(),
                detailMessage,
                path,
                null
        );
        return new ResponseEntity<>(errorResponse,status);
    }

    // * tratar as anotações de validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValdationErrors(MethodArgumentNotValidException ex,WebRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        String detailMessage = "Erro em um ou mais campos!";
        String path = request.getDescription(false).replace("uri=","");

        if(ex.getCause() != null) {
            detailMessage = ex.getCause().getMessage();
        }

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                status.value(),
                status.getReasonPhrase(),
                detailMessage,
                path,
                errors
        );
        return new ResponseEntity<>(errorResponse,status);
    }
}
