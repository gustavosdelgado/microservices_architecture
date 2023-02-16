package io.github.gustavosdelgado.microorder.exception;

public class BadRequestException extends Exception {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException() {
        super();
    }

}
