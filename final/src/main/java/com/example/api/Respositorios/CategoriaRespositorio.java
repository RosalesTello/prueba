package com.example.api.Respositorios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.Entidades.Categoria;

@Repository
public interface CategoriaRespositorio extends JpaRepository<Categoria,String>{

}
