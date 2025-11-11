package com.desafio.gerenciamento.handler;

public class ProjectExists extends RuntimeException {
    public ProjectExists(String message) {
        super(message);
    }
}
