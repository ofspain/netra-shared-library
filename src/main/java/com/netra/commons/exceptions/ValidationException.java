package com.netra.commons.exceptions;

import com.netra.commons.exceptions.BaseException;

public class ValidationException extends RuntimeException implements BaseException {

    private String message;

    public ValidationException(String message){
        this.message = message;
    }

    public String getAppErrorCode(){
        return "ERR_VAL";
    }

}
