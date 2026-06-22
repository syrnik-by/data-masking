package ru.yourcompany.masking.common.exception;

public class MaskingException extends RuntimeException {

    public MaskingException(String message) {
        super(message);
    }

    public MaskingException(String message, Throwable cause) {
        super(message, cause);
    }
}
