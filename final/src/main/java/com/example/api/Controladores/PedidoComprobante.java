package com.example.api.Controladores;

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

import com.example.api.Entidades.ComprobantedePago;
import com.example.api.Sericio.ComprobantedePago_Pedido;

@RequestMapping("pedido/comprobante")
@RestController
public class PedidoComprobante {
	
	 @Autowired
	    private ComprobantedePago_Pedido comprobanteService;

	    // Agregar un producto al carrito de compra
	    @PostMapping("/agregaralcarrito")
	    public ResponseEntity<Object> agregaralcarrito(@RequestBody ComprobantedePago comprobante) {
	        return comprobanteService.agregar(comprobante);
	    }

	    // Eliminar un producto del carrito de compras
	    @DeleteMapping("/eliminarproductodelcarrito/{nombreProducto}")
	    public ResponseEntity<Object> eliminarproductodelcarrito(@PathVariable String nombreProducto) {
	        return comprobanteService.eliminar(nombreProducto);
	    }

	    // Actualizar un producto en el carrito de compras
	    @PutMapping("/actualizarProductodelCarrito")
	    public ResponseEntity<Object> actualizarProductodelCarrito(@RequestBody ComprobantedePago comprobanteActualizado) {
	        return comprobanteService.actualizarComprobante(comprobanteActualizado);
	    }

	    // Filtrar un producto por su id en el carrito
	    @GetMapping("/filtradoProductoDelCarrito/{nombreProducto}")
	    public ResponseEntity<Object> filtradoProductoDelCarrito(@PathVariable String nombreProducto) {
	        return comprobanteService.filtradoproductoporcarrito(nombreProducto);
	    }

	    // Vaciar el carrito de compras
	    @DeleteMapping("/vaciarCarrito")
	    public ResponseEntity<Object> vaciarCarrito() {
	        return comprobanteService.vaciarCarrito();
	    }

	    // Buscar pedidos de un usuario por correo
	    @GetMapping("/buscar/{correo}")
	    public ResponseEntity<Object> buscarPedidosPorCorreo(@PathVariable String correo) {
	        return comprobanteService.buscarpedidoporcorreo(correo);
	    }

	    // Guardar un pedido
	    @PostMapping("/guardar/{correo}")
	    public ResponseEntity<Object> guardarPedido(@PathVariable String correo) {
	        return comprobanteService.guardarPedido(correo);
	    }
	}


