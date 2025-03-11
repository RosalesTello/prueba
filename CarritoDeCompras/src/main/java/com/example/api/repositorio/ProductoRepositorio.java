package com.example.api.repositorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.api.entidades.Producto;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, String> {

}
