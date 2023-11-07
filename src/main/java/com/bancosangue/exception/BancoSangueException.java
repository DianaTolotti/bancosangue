package com.bancosangue.exception;

public class BancoSangueException extends RuntimeException {

    public BancoSangueException(String message, Exception e) {
        super(message);
    }
}
