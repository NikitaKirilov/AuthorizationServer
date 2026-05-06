package org.example.backend.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestUtils {

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    public String getIpAddress(HttpServletRequest request) {
        String header = request.getHeader(X_FORWARDED_FOR);
        return header != null && !header.isBlank()
                ? header.split(",")[0].trim()
                : request.getRemoteAddr();
    }
}
