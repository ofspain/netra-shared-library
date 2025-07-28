package com.netra.commons.exceptions;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
public class ResourceNotFoundException  extends RuntimeException implements BaseException{

    private final String resource;
    private final String resourceId;


    public ResourceNotFoundException(String resource, String resourceId) {
        super();
        this.resource = resource;
        this.resourceId = String.format("id [%s]", resourceId);
    }

    public ResourceNotFoundException(String resource, Object resourceId, String resourceIdName) {
        super();
        this.resource = resource;
        this.resourceId = String.format("%s [%s]", resourceIdName, resourceId);
    }

    public ResourceNotFoundException(String resource, Map<String, Object> parameters) {
        super();
        this.resource = resource;
        List<String> params = parameters.entrySet().stream()
                .map(m -> String.format("%s [%s]", m.getKey(), m.getValue()))
                .collect(Collectors.toList());
        this.resourceId = StringUtils.join(params, ", ");
    }

    public String getMessageCode() {
        return "resource.not.found";
    }

    public Object[] getMessageArguments() {
        return new Object[]{resource, resourceId};
    }

    @Override
    public String getMessage() {
        return String.format("%s with %s not found", resource, resourceId);
    }

    public String getAppErrorCode(){
        return "NOT_FOUND_ERR";
    }
}
