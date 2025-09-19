package com.netra.commons.models.endpoint;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ApiKeyAuth.class, name = "API_KEY"),
        @JsonSubTypes.Type(value = BearerTokenAuth.class, name = "BEARER"),
        @JsonSubTypes.Type(value = BasicAuth.class, name = "BASIC"),
        @JsonSubTypes.Type(value = MtlsAuth.class, name = "MTLS"),
        @JsonSubTypes.Type(value = CustomSignatureAuth.class, name = "CUSTOM_SIGNATURE")
})
public interface AuthConfig {
    AuthType getAuthType();

    public enum AuthType {
        NONE,
        API_KEY,
        BEARER,
        BASIC,
        MTLS,
        CUSTOM_SIGNATURE
    }
}
