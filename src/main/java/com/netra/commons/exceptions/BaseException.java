package com.netra.commons.exceptions;

public interface BaseException {
    default public String getAppErrorCode(){
        return "ERR";
    }
}
