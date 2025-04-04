package com.example.api.Respositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.Entidades.Producto;
@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, String> {

}
