package com.example.api.Controladores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.Entidades.Producto;
import com.example.api.Sericio.ProductoServicio;

@RequestMapping("/productos")
@RestController
public class ProductoControlador {
	
	 @Autowired ProductoServicio productoServicio;
	    
	    @GetMapping("/listar")
	    public ResponseEntity<Object> listarProductos() {
	        return productoServicio.listado();
	    }
	    
	    @PostMapping("/agregar")
	    public ResponseEntity<Object> agregarProducto(@RequestBody Producto producto) {
	        return productoServicio.Agregar(producto);
	    }
	    
	    @PutMapping("/editar")
	    public ResponseEntity<Object> editarProducto(@RequestBody Producto producto) {
	        return productoServicio.Editar(producto);
	    }

}
