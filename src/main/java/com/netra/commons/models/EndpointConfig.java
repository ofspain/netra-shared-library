package com.netra.commons.models;

import com.netra.commons.enums.DomainType;

import java.util.*;
import java.util.regex.Pattern;

public class EndpointConfig extends BaseEntity {


    private String domainCode; // e.g. "BANK_GTB", "MONNIFY", etc.

    private DomainType domainType;

    private String baseUrl;

    private boolean useProxy;

    private boolean requiresAuth;

    private AuthType authType = AuthType.NONE;

    private int timeoutMillis;

    private ProtocolType protocolType = ProtocolType.HTTPS; // default to HTTPS

    // Stored as a single delimited string in DB
    private String uniqueTransactionParams;

    private String uniqueTransactionUrl;

    // To be manually hydrated when needed
    private List<EndpointHeader> uniqueTransactionHeaders;

    private String multipleTransactionParams;

    private String multipleTransactionUrl;

    // To be manually hydrated when needed
    private List<EndpointHeader> multipleTransactionHeaders;

    private String description;

    // ========= ENUMS ========= //

    public enum ProtocolType {
        HTTP,
        HTTPS
    }

    public enum AuthType {
        NONE,
        API_KEY,
        BEARER_TOKEN,
        BASIC,
        MTLS
    }

    public enum ParamSeparator {
        UNIQUE_PARAM_SEPARATOR("<UTPS>"),
        MULTIPLE_PARAM_SEPARATOR("<MTPS>");

        private final String separator;

        ParamSeparator(String separator) {
            this.separator = separator;
        }

        public String getSeparator() {
            return separator;
        }
    }

    // ========= STATIC HELPERS ========= //

    public static List<String> parseParams(String paramStr, ParamSeparator separatorType) {
        if (paramStr == null || paramStr.isEmpty()) return Collections.emptyList();
        return Arrays.asList(paramStr.split(Pattern.quote(separatorType.separator)));
    }

    public static String joinParams(List<String> params, ParamSeparator separatorType) {
        if (params == null || params.isEmpty()) return "";
        return String.join(separatorType.separator, params);
    }

    public List<String> getUniqueTransactionParamList() {
        return parseParams(this.uniqueTransactionParams, ParamSeparator.UNIQUE_PARAM_SEPARATOR);
    }

    public void setUniqueTransactionParamList(List<String> params) {
        this.uniqueTransactionParams = joinParams(params, ParamSeparator.UNIQUE_PARAM_SEPARATOR);
    }

    public List<String> getMultipleTransactionParamList() {
        return parseParams(this.multipleTransactionParams, ParamSeparator.MULTIPLE_PARAM_SEPARATOR);
    }

    public void setMultipleTransactionParamList(List<String> params) {
        this.multipleTransactionParams = joinParams(params, ParamSeparator.MULTIPLE_PARAM_SEPARATOR);
    }

    // ========= Getters and Setters ========= //

    // (Omitted here for brevity — generate with your IDE or Lombok if allowed)

}

