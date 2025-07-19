package com.netra.commons.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

@Component
public class SecretCryptoUtil {

    private final String SECRET_KEY;

    @Autowired
    public SecretCryptoUtil(@Value("${password-encryption-key}") String secretKey) {
        this.SECRET_KEY = secretKey;
    }
    private static final String INIT_VECTOR = "abcdef9876543210"; // 16-char IV for AES/CBC

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    private SecretKey getSecretKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
    }

    private static IvParameterSpec getIv() {
        return new IvParameterSpec(INIT_VECTOR.getBytes());
    }

    public String encrypt(String rawText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), getIv());
            byte[] encrypted = cipher.doFinal(rawText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt text", e);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), getIv());
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            byte[] original = cipher.doFinal(decoded);
            return new String(original);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt text", e);
        }
    }
}

