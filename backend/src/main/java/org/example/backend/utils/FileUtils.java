package org.example.backend.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtils {

    private static final byte[] PNG_SIGNATURE = new byte[]{(byte) 137, 80, 78, 71, 13, 10, 26, 10};

    public static boolean isPngSignatureValid(byte[] image) {
        for (int i = 0; i < PNG_SIGNATURE.length; i++) {
            if (PNG_SIGNATURE[i] != image[i]) {
                return false;
            }
        }

        return true;
    }
}
