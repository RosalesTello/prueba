package com.example.demo.controlador;

import java.security.Principal;
import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.seguridad.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class Privado {

    private final JwtTokenProvider jwtTokenProvider;

    public Privado(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public String inicio() {
        return "<h1>Bienvenido</h1>"
                + "<p><a href='/oauth2/authorization/google'>Iniciar sesión con Google</a></p>";
    }
    //

    // Ruta pública accesible sin autenticación
    @GetMapping("/publico")
    public String rutaPublica() {
        return "Bienvenido a la ruta pública.";
    }

    // Ruta privada protegida (requiere autenticación con Google)
    @GetMapping("/privado")
    public ResponseEntity<String> rutaPrivada(@AuthenticationPrincipal OAuth2User usuario) {
        if (usuario == null) {
            return ResponseEntity.status(401).body("Error: No autenticado.");
        }
        return ResponseEntity.ok("Bienvenido, " + usuario.getAttribute("name"));
    }

    // Obtener datos del usuario autenticado
    @GetMapping("/privado/datos")
    public ResponseEntity<Map<String, Object>> datosUsuario(@AuthenticationPrincipal OAuth2User usuario) {
        if (usuario == null) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(usuario.getAttributes());
    }

    
    @GetMapping("/logout-google")
    public RedirectView logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // Invalida la sesión en la aplicación
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        // Redirige a la página de logout de Google
        return new RedirectView("https://accounts.google.com/Logout");
    }
    
}
