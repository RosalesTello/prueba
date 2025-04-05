package com.example.api.Sericio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.api.Entidades.Categoria;
import com.example.api.Respositorios.CategoriaRespositorio;

@Service
public class CategoriaServicio {
	@Autowired CategoriaRespositorio categoriarepo;
	
	public ResponseEntity<Object>Listado()
	{
		List<Categoria>categorias=categoriarepo.findAll();
		if(categorias.isEmpty())
		{
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(categorias);
		
	}
	
	public ResponseEntity<Object>agregar(Categoria categoria)
	{
		Categoria categorias=categoriarepo.findById(categoria.getNombreCategoria()).orElse(null);
		if(categorias==null)
		{
			categoria.setEstado("Activo");
		    categoriarepo.save(categoria);
		    return ResponseEntity.ok("Producto Guardado Correctamente");
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede Repetir la categoria");
		
	}
	

}
