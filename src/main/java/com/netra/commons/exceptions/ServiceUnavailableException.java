package com.netra.commons.exceptions;

public class ServiceUnavailableException  extends RuntimeException implements BaseException{
    public ServiceUnavailableException(String message) {
        super(message);
    }
    public String getAppErrorCode(){
        return "SRV_ERR";
    }
}
