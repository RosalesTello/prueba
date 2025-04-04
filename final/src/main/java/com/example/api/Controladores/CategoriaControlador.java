package com.example.api.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.Entidades.Categoria;
import com.example.api.Sericio.CategoriaServicio;

@RequestMapping("/Categorias")
@RestController
public class CategoriaControlador {
	
	@Autowired CategoriaServicio categoriaservicio;
	
	 @GetMapping("/listar")
	    public ResponseEntity<Object> listarCategorias() {
	        return categoriaservicio.Listado();
	    }

	    @PostMapping("/agregar")
	    public ResponseEntity<Object> agregarCategoria(@RequestBody Categoria categoria) {
	        return categoriaservicio.agregar(categoria);
	    }

}
