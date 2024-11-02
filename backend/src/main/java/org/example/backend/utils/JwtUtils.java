package org.example.backend.utils;

import lombok.experimental.UtilityClass;
import org.example.backend.models.entities.Scope;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class JwtUtils {

    private static final String JWT_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    public static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(JWT_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    public static Map<String, List<String>> getResourceAccessClaim(List<Scope> scopes) {
        return scopes.stream().collect(Collectors.groupingBy(
                Scope::getResourceName,
                Collectors.mapping(Scope::getName, Collectors.toList())
        ));
    }
}
