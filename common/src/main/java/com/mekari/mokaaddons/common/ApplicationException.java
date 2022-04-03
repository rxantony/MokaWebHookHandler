package com.mekari.mokaaddons.common;

public class ApplicationException extends Exception {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(Throwable inner) {
        super(inner);
    }

    public ApplicationException(String message, Throwable inner) {
        super(message, inner);
    }
}
