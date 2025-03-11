package com.example.api.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.entidades.ListaDeProductos;
@Repository
public interface ListaDeProductosRepositorio extends JpaRepository<ListaDeProductos, Integer>{

}
