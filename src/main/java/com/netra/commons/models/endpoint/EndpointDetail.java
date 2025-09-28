package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class EndpointDetail {
    private String url; // path relative to baseUrl or absolute
    private HTTPMethod method;
    private List<String> pathParamKeys = new ArrayList<>();
    private List<String> queryParamKeys = new ArrayList<>();

    private OperationType operationType;

    // persisted template headers (may contain ${vault:<id>} placeholders)
    private List<StaticHeader> headers = new ArrayList<>();

    // runtime inputs the caller must supply
    private List<DynamicHeader> dynamicHeaders = new ArrayList<>();

    // request body template (may contain vault placeholders or path param placeholders)
    private String requestBodyTemplate;

    // contract for runtime inputs beyond headers (form fields, body keys etc)
    private RuntimeContract runtimeContract;

    public enum HTTPMethod {
        //todo: note only non-mutable calls will be made
        GET,
        POST,
        PUT,
        DELETE
    }

    public enum OperationType{


        UNIQUE_TRANSACTION_SEARCH("uniqueTransaction"), BULK_TRANSACTION_SEARCH("bulkTransaction");
        private final String value;
        OperationType(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }
    }
}