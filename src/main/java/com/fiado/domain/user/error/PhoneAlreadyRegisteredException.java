package com.fiado.domain.user.error;

public class PhoneAlreadyRegisteredException extends RuntimeException {
    public PhoneAlreadyRegisteredException() {
        super("Número inválido");
    }
}
