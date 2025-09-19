package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MtlsAuth implements AuthConfig {
    private final AuthType authType = AuthType.MTLS;

    // Client cert & key (base64 stored in vault)
    private String certVaultAlias;        // vault: path to client certificate (base64)
    private String keyVaultAlias;         // vault: path to private key (base64)
    private String keyPasswordVaultAlias; // optional, password for private key if any

    // Trust store (base64 stored in vault)
    private String trustStoreVaultAlias;    // vault: path to trust store file (base64)
    private String trustStorePasswordAlias; // vault: path to trust store password
    private String trustStoreType = "PKCS12"; // default type, can be overridden

    // Optional local file names if library requires
    private String keyStoreFileName;       // optional, e.g., "client.p12"
    private String trustKeyFileName;       // optional, e.g., "truststore.jks"
}

//runtime resolution
//byte[] keystoreBytes = Base64.getDecoder().decode(vault.getSecret(certVaultAlias));
//KeyStore ks = KeyStore.getInstance("PKCS12");
//ks.load(new ByteArrayInputStream(keystoreBytes), keyPassword.toCharArray());

//{
//  "authType": "MTLS",
//
//  // --- Client certificate & key (mandatory) ---
//  "certVaultAlias": "vault:secrets/mutual-tls/client-cert",
//  "keyVaultAlias": "vault:secrets/mutual-tls/client-key",
//  "keyPasswordVaultAlias": "vault:secrets/mutual-tls/client-key-password",  // optional if key is encrypted
//
//  // --- Trust store (optional but recommended) ---
//  "trustStoreVaultAlias": "vault:secrets/mutual-tls/truststore",
//  "trustStorePasswordAlias": "vault:secrets/mutual-tls/truststore-password",
//  "trustStoreType": "PKCS12",  // default type; could also be JKS
//
//  // --- Optional local file names if required by library ---
//  "keyStoreFileName": "client.p12",
//  "trustKeyFileName": "truststore.jks"
//}