package com.netra.commons.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ApplicationException extends LogKeyException {

    private final String logKey = ExceptionUtil.generateErrorLogKey("ISE");
    @Getter
    private final HttpStatus status;

    public ApplicationException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR; // default
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR; // default
    }

    public ApplicationException(String message, HttpStatus status) {
        super(message);
        this.status = status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ApplicationException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getLogKey() {
        return logKey;
    }

    @Override
    public String getAppErrorCode() {
        return "server.error";
    }

}
