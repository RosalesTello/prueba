package com.example.demo.seguridad;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
@Component
public class JwtTokenProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final Key key;
    private final long expirationMs = 86400000; // 1 día

    public JwtTokenProvider() {
        // Se recomienda que la clave tenga al menos 32 bytes para HMAC SHA-256
        this.key = Keys.hmacShaKeyFor("mi_secreto_super_seguro_muy_largo".getBytes(StandardCharsets.UTF_8));
    }

    // Generar token JWT
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    // Obtener autenticación desde el token
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
        
        UserDetails userDetails = new User(claims.getSubject(), "", List.of());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Validar token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("Token expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token no soportado: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Token mal formado: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Firma inválida: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Token vacío o nulo: {}", e.getMessage());
        }
        return false;
    }
}

