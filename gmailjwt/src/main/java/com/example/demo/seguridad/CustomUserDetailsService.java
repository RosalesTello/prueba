package com.example.demo.seguridad;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

//manualmente 
@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	 @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	        // Como estás usando OAuth2, solo devolvemos un usuario con el email autenticado
	        return new User(email, "", Collections.emptyList()); 
	    }

}
