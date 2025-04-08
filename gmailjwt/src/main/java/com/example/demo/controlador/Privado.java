package com.example.demo.controlador;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.entidades.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.seguridad.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RequestMapping("/")
@Controller
public class Privado {

	@Autowired UsuarioRepository repousuario;
    private final JwtTokenProvider jwtTokenProvider;

    public Privado(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Página de inicio con enlace para login con Google
    @GetMapping
    public String inicio() {
         return "inicio";
    }


    // Nueva ruta donde se mostrará el usuario autenticado y su JWT
    @GetMapping("/perfil")
    public String perfil( @AuthenticationPrincipal OAuth2User usuario) 
    {
    	//aca llamo la instancia de para consumir ese metodo nad mas 
        
        return "PantallaPrincipal"; // Esto busca una vista en templates o WEB-INF
    }

    // Cerrar sesión en Google y limpiar la sesión local
    @GetMapping("/logout-google")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();  // Limpiar el contexto de seguridad
        request.getSession().invalidate();     // Invalidar la sesión

        // Redirigir directamente a la autenticación de Google para iniciar sesión nuevamente
        return "redirect:/oauth2/authorization/google";
    }


    
    ////aca
    
    @GetMapping("/acceso")
    public ResponseEntity<?> accederRutaPrivada(HttpServletRequest request) {
        // Obtener el token del header "Authorization"
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Error: Token no proporcionado o inválido.");
        }

        // Extraer solo el token (sin "Bearer ")
        String token = authHeader.substring(7);

        // Validar el token
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).body("Error: Token inválido o expirado.");
        }

        // Si el token es válido
        return ResponseEntity.ok("✅ Accediste a esta ruta privada con el JWT de Gmail.");
    }
    
    
    @GetMapping("/incognito")
    public String apublico() {
        // Obtener el token del header "Authorization"
       
        // Si el token es válido
        return "tu si ";
    }
    
    
}

