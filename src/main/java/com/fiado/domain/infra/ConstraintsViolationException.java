package com.fiado.domain.infra;

import lombok.Getter;

import java.util.Map;

@Getter
public class ConstraintsViolationException extends RuntimeException {
    private final Map<String, String> errors;

    public ConstraintsViolationException(Map<String, String> errors) {
        super("Validação falhou");
        this.errors = errors;
    }
}
