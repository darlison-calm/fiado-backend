package com.fiado.domain.user.exception;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.Map;

public class UserConstraintErrorHandler {
    public static Map<String, String> parse(DataIntegrityViolationException e) {
        String errorMessage = e.getMostSpecificCause().getMessage();
        Map<String, String> errors = new HashMap<>();

        if (errorMessage.contains("users_unique_email_idx")) {
            errors.put("email", "Email não disponível");
        }
        if (errorMessage.contains("users_unique_username_idx")) {
            errors.put("username", "Nome de usuário já existe");
        }
        if (errorMessage.contains("users_unique_phone_number_idx")) {
            errors.put("phoneNumber", "Telefone já cadastrado");
        }
        return errors;
    }
}
