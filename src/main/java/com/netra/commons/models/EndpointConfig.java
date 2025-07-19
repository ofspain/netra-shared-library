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

    private String proxyHost;
    private Integer proxyPort;
    private String proxyUsername;


    // ========= ENUMS ========= //

    public enum ProtocolType {
        HTTP,
        HTTPS,

        SOCKS

    }

    public enum ProxyType {
        HTTP,
        SOCKS
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public DomainType getDomainType() {
        return domainType;
    }

    public void setDomainType(DomainType domainType) {
        this.domainType = domainType;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean isUseProxy() {
        return useProxy;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    public boolean isRequiresAuth() {
        return requiresAuth;
    }

    public void setRequiresAuth(boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public String getUniqueTransactionParams() {
        return uniqueTransactionParams;
    }

    public void setUniqueTransactionParams(String uniqueTransactionParams) {
        this.uniqueTransactionParams = uniqueTransactionParams;
    }

    public String getUniqueTransactionUrl() {
        return uniqueTransactionUrl;
    }

    public void setUniqueTransactionUrl(String uniqueTransactionUrl) {
        this.uniqueTransactionUrl = uniqueTransactionUrl;
    }

    public List<EndpointHeader> getUniqueTransactionHeaders() {
        return uniqueTransactionHeaders;
    }

    public void setUniqueTransactionHeaders(List<EndpointHeader> uniqueTransactionHeaders) {
        this.uniqueTransactionHeaders = uniqueTransactionHeaders;
    }

    public String getMultipleTransactionParams() {
        return multipleTransactionParams;
    }

    public void setMultipleTransactionParams(String multipleTransactionParams) {
        this.multipleTransactionParams = multipleTransactionParams;
    }

    public String getMultipleTransactionUrl() {
        return multipleTransactionUrl;
    }

    public void setMultipleTransactionUrl(String multipleTransactionUrl) {
        this.multipleTransactionUrl = multipleTransactionUrl;
    }

    public List<EndpointHeader> getMultipleTransactionHeaders() {
        return multipleTransactionHeaders;
    }

    public void setMultipleTransactionHeaders(List<EndpointHeader> multipleTransactionHeaders) {
        this.multipleTransactionHeaders = multipleTransactionHeaders;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public ProxyType getProxyType() {
        return proxyType;
    }

    public void setProxyType(ProxyType proxyType) {
        this.proxyType = proxyType;
    }

    private String proxyPassword;
    private ProxyType proxyType = ProxyType.HTTP; // Optional enum

    // ========= Getters and Setters ========= //

    // (Omitted here for brevity — generate with your IDE or Lombok if allowed)

}

