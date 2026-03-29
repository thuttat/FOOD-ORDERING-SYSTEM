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
import tools.jackson.databind.ObjectMapper;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log=LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper=objectMapper;
    }

    @Override
    public void commence (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
    
    throws IOException, ServletException{
        log.debug("Unauthorized: {} {}", request.getRequestURI(),authException.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiError body=new ApiError(
            "Unauthorizied",
            "Phien dang nhap da het han/ khong hop le. Vui long dang nhap lai",
            null,
            request.getRequestURI(),
            Instant.now()
        );
        objectMapper.writeValue(response.getOutputStream(), body);
    }

    
}

