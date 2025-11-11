package com.desafio.gerenciamento.model;


public enum Status {
    TODO(1),
    DOING(2),
    DONE(3);

    private int value;
    Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
