package com.sosim.server.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sosim.server.config.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            setErrorResponse(response, e);
        }
    }

    public void setErrorResponse(HttpServletResponse response, CustomException e) throws IOException {
        response.setStatus(e.getCodeType().getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", e.getCodeType().getCode());
        body.put("error", e.getMessage());

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
