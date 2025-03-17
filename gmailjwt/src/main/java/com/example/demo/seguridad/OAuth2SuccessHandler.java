package com.example.demo.seguridad;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                                        Authentication authentication) throws IOException {
        OAuth2User usuario = (OAuth2User) authentication.getPrincipal();

        // 🔥 Obtener el email del usuario autenticado
        String email = usuario.getAttribute("email");

        // 🔥 Generar el JWT con el email
        String token = jwtTokenProvider.generarToken(email);

        // 🔹 Crear la respuesta con el email y el JWT
        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("email", email);
        jsonResponse.put("token", token);

        // 🔹 Enviar la respuesta en formato JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(jsonResponse));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

