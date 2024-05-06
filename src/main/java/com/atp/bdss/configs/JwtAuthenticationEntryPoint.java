package com.atp.bdss.configs;

import java.io.IOException;
import com.atp.bdss.dtos.responses.ResponseData;
import com.atp.bdss.utils.ErrorsApp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorsApp errorsApp = ErrorsApp.UNAUTHENTICATED;

        response.setStatus(errorsApp.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ResponseData responseData = ResponseData
                .builder()
                .code(errorsApp.getCode())
                .message("Error")
                .data(errorsApp.getDescription())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        log.error("Exception");

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
        response.flushBuffer();
    }
}
