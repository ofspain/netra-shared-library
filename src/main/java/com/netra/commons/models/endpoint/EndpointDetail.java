package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class EndpointDetail {
    private String url; // path relative to baseUrl or absolute
    private HTTPMethod method;
    private List<String> pathParamKeys;
    private List<String> queryParamKeys;

    // persisted template headers (may contain ${vault:<id>} placeholders)
    private List<StaticHeader> headers;

    // runtime inputs the caller must supply
    private List<DynamicHeader> dynamicHeaders;

    // request body template (may contain vault placeholders or path param placeholders)
    private String requestBodyTemplate;

    // contract for runtime inputs beyond headers (form fields, body keys etc)
    private RuntimeContract runtimeContract;
}