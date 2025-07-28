package com.netra.commons.exceptions;

public abstract class LogKeyException extends RuntimeException implements BaseException{

    protected LogKeyException(String message) {super(message);}

    protected LogKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract String getLogKey();
}
