package org.example.backend.utils;

import lombok.experimental.UtilityClass;
import org.example.backend.exceptions.FileValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@UtilityClass
public class FileUtils {

    private static final long MAX_PNG_SIZE = 1024L * 1024L;
    private static final byte[] PNG_SIGNATURE = new byte[]{(byte) 137, 80, 78, 71, 13, 10, 26, 10};

    public static void validatePngImage(MultipartFile file) throws IOException {
        if (file == null) {
            return;
        }

        if (file.isEmpty()) {
            throw new FileValidationException("File is empty");
        }

        if (file.getSize() > MAX_PNG_SIZE) {
            throw new FileValidationException("File size must be less then 1mb");
        }

        if (!isPngSignatureValid(file.getBytes())) {
            throw new FileValidationException("File is not a PNG file");
        }
    }

    private static boolean isPngSignatureValid(byte[] image) {
        for (int i = 0; i < PNG_SIGNATURE.length; i++) {
            if (PNG_SIGNATURE[i] != image[i]) {
                return false;
            }
        }

        return true;
    }
}
