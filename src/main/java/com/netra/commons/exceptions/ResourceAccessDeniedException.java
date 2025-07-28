package com.netra.commons.exceptions;

import lombok.Getter;

@Getter
public class ResourceAccessDeniedException extends RuntimeException implements BaseException {

    private final String resource;
    private final String resourceId;

    public ResourceAccessDeniedException(String message, String resource, String resourceId) {
        super(message);
        this.resource = resource;
        this.resourceId = resourceId;
    }

    public Object[] getMessageArguments() {
        return new Object[]{resource, resourceId};
    }

    public String getAppErrorCode(){
        return "ACCESS_DENIED_ERR";
    }
}
