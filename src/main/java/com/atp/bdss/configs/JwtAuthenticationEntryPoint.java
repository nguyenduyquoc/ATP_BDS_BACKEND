package com.atp.bdss.configs;

import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.utils.ErrorsApp;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorsApp errorsApp = ErrorsApp.UNAUTHENTICATED;

        response.setStatus(errorsApp.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();

        ResponseData responseData = ResponseData
                .builder()
                .code(errorsApp.getCode())
                .message("Error")
                .data(errorsApp.getDescription())
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(responseData));
        response.flushBuffer();
    }
}
