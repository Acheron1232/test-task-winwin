package com.acheron.serviceb.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TransformService {

    @Value("${header_secret}")
    private String headerSecret;

    public String transform(String text, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String header = request.getHeader("X-Internal-Token");
        if (header == null || !header.equals(headerSecret)) {
            response.sendError(403, "Invalid/missing header");
            return "";
        }
        //transforming "text" -> "TeXt"
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (i % 2 == 0) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }
}
