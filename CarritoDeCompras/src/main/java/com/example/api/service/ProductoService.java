package com.example.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.api.entidades.Producto;
import com.example.api.repositorio.ProductoRepositorio;

@Service
public class ProductoService {
	
	@Autowired ProductoRepositorio productorepositorio;
	
	 public ResponseEntity<Object> crearProducto(Producto producto) {
	        Producto productoexistente = productorepositorio.findById(producto.getNombre()).orElse(null);
	        if (productoexistente!=null) {
	            return ResponseEntity.status(HttpStatus.CONFLICT).body("El producto con nombre '" + producto.getNombre() + "' ya existe.");
	        }
	        
	        productorepositorio.save(producto);
	        return ResponseEntity.ok("Producto '" + producto.getNombre() + "' creado exitosamente.");
	    }
	

}
