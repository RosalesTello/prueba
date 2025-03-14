package com.example.demo.seguridad;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
//mabuales las depencias 
@Component

public class JwtAuthenticationFilter  extends OncePerRequestFilter {

		
	private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // Si ya hay una autenticación (por ejemplo, de OAuth2), no apliques JWT
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = resolveToken(request);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                var auth = jwtTokenProvider.getAuthentication(token);
                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }
}
