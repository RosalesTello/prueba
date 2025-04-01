package com.example.api.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.entidades.ListaDeProductos;
import com.example.api.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	
	 	@Autowired
	    private PedidoService pedidoService;

	   
	    @PostMapping("/agregaralcarrito")
	    public ResponseEntity<Object> agregarProducto(@RequestBody ListaDeProductos pedidoTemporal) {
	        return pedidoService.agregarProductoALista(pedidoTemporal);
	    }

	    @PostMapping("/guardarpedido")
	    public ResponseEntity<Object> guardarPedido() {
	        return pedidoService.guardarPedido();
	    }
	    
	    
	    @GetMapping("filtrado/{id}")
	    public ResponseEntity<Object>filtrado(@PathVariable int id)
	    {
	    	return pedidoService.filtrado(id);
	    }
	    
	    @DeleteMapping("/vaciar-carrito")
	    public ResponseEntity<Object> vaciarCarrito() {
	        return pedidoService.vaciarCarrito();
	    }

	    
	    @DeleteMapping("/eliminar-producto/{nombreProducto}")
	    public ResponseEntity<Object> eliminarProductoDelCarrito(@PathVariable String nombreProducto) {
	        return pedidoService.eliminarProductoDelCarrito(nombreProducto);
	    }

	    // Nuevo método para obtener todos los productos del carrito
	    @GetMapping("/obtener-carrito")
	    public ResponseEntity<Object> obtenerCarrito() {
	        return pedidoService.obtenerCarrito();
	    }
	    
	    @PutMapping("/actualizarproducto")
	    public ResponseEntity<Object> actualizarProductoEnCarrito(@RequestBody ListaDeProductos productoActualizado) {
	        return pedidoService.actualizarProductoEnCarrito(productoActualizado);
	    }
	    
	    @GetMapping("/filtradoporcarrito/{nombre}")
	    public ResponseEntity<Object> filtradoporcarrito(@PathVariable String  nombre) {
	    	return pedidoService.filtradoCarrito(nombre);
	    
	    }
	      
	    
	    
	    
}
