package com.fiado.domain.user.error;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException() {
            super("Email já esta em uso");
    }
}
