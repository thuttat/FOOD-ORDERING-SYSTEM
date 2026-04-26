package duckie.example.backend.security;

import java.io.IOException;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import duckie.example.backend.dto.ApiError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper; // Giữ nguyên package của bạn

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        log.error("Error authenticated {}: {}", request.getRequestURI(), authException.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String errorMessage = authException.getMessage();


        if ("Bad credentials".equalsIgnoreCase(errorMessage)) {
            errorMessage = "Email or password is incorrect.";
        } else if (errorMessage != null && errorMessage.toLowerCase().contains("expired")) {
            errorMessage = "Sign in again pls your session was exred";
        } else if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = "Pls fill your token (Token).";
        }

        ApiError body = new ApiError(
                "Unauthorized",
                errorMessage,
                null,
                request.getRequestURI(),
                Instant.now()
        );

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}