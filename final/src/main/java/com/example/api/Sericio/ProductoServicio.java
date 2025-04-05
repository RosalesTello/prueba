package com.example.api.Sericio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.api.Entidades.Categoria;
import com.example.api.Entidades.Producto;
import com.example.api.Respositorios.CategoriaRespositorio;
import com.example.api.Respositorios.ProductoRepositorio;

@Service
public class ProductoServicio {
	
	@Autowired ProductoRepositorio productorepo;
	@Autowired CategoriaRespositorio categoriaRepo;
	public ResponseEntity<Object>listado()
	{
		List<Producto>productos=productorepo.findAll();
		if(productos.isEmpty())
		{
			return ResponseEntity.noContent().build();	
		}
		
		return ResponseEntity.ok(productos);
	}
	
	public ResponseEntity<Object>Agregar(Producto producto)
	{
		Categoria categoria=categoriaRepo.findById(producto.getCategoria().getNombreCategoria()).orElse(null);
		if (categoria==null) {
			
			return ResponseEntity.notFound().build();
		}
		
		Producto productoAgregar=productorepo.findById(producto.getNombre()).orElse(null);
		if(productoAgregar==null)
		{
			productorepo.save(producto);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body("No se peude agregar un Producto Repetido");
	}
	
	public ResponseEntity<Object> Editar(Producto productoNuevo) {
	    Producto productoExistente = productorepo.findById(productoNuevo.getNombre()).orElse(null);
	    if (productoExistente != null) {
	    	productoExistente.setNombre(productoNuevo.getNombre());
	        productoExistente.setPrecio(productoNuevo.getPrecio());
	        productoExistente.setStock(productoNuevo.getStock());
	        productoExistente.setFoto(productoNuevo.getFoto());
	        productorepo.save(productoExistente);
	        return ResponseEntity.ok("Producto actualizado correctamente");
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
	}

	

}
