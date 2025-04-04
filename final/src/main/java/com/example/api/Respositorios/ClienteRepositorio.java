package com.example.api.Respositorios;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.Entidades.Cliente;
@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String> {
	Optional<Cliente>findByCorreo(String correo);

}
