package com.example.api.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.entidades.Producto;
import com.example.api.service.ProductoService;

@RestController
@RequestMapping("Productos")
public class ProductoController {

	@Autowired
	    private ProductoService productoService;

	    @PostMapping("/crear")
	    public ResponseEntity<Object> crearProducto(@RequestBody Producto producto) {
	        return productoService.crearProducto(producto);
	    }
}
