package com.example.api.Sericio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.api.Entidades.ComprobantedePago;
import com.example.api.Entidades.Pedido;
import com.example.api.Respositorios.ComprobantedePagoRepositorio;
import com.example.api.Respositorios.PedidoRepositorio;
import com.example.api.Respositorios.ProductoRepositorio;
import com.example.api.Respositorios.TarjetaRepositorio;
import com.example.api.Entidades.Producto;
import com.example.api.Entidades.Tarjeta;


@Service
public class ComprobantedePago_Pedido {
	@Autowired ComprobantedePagoRepositorio comprobanteRepo;
	@Autowired PedidoRepositorio pedidoRepo;
	@Autowired ProductoRepositorio productoRepo;
	@Autowired TarjetaRepositorio tarjetaRepo;
	@Autowired EmailService emailService;
	
	
	private final List<ComprobantedePago> lista= new ArrayList<>();
	
	public ResponseEntity<Object> agregar(ComprobantedePago comprobantepago) {
	    Producto productoEnComprobante = productoRepo.findById(comprobantepago.getProducto().getNombre()).orElse(null);

	    if (productoEnComprobante == null) {
	        return ResponseEntity.notFound().build();
	    }

	    if (productoEnComprobante.getStock() >= comprobantepago.getCantidad()) {
	        comprobantepago.setPrecioUnitario(productoEnComprobante.getPrecio());
	        comprobantepago.setPrecioTotal(productoEnComprobante.getPrecio() * comprobantepago.getCantidad());

	        productoEnComprobante.setStock(productoEnComprobante.getStock() - comprobantepago.getCantidad());
	        productoRepo.save(productoEnComprobante);

	        lista.add(comprobantepago);

	        return ResponseEntity.ok(lista);
	    }

	    return ResponseEntity.badRequest().body("Stock insuficiente");
	}
	
	
	//eliminar
	public ResponseEntity<Object> eliminar(String nombreProducto) {
	    for (ComprobantedePago comprobante : lista) {
	        if (comprobante.getProducto().getNombre().equals(nombreProducto)) {
	            Producto producto = productoRepo.findById(comprobante.getProducto().getNombre()).orElse(null);
	            if (producto != null) {
	                producto.setStock(producto.getStock() + comprobante.getCantidad());
	                productoRepo.save(producto);  // Guardar los cambios de stock
	                lista.remove(comprobante);
	                return ResponseEntity.ok("Producto Eliminado del Carrito");
	            }
	        }
	    }
	    return ResponseEntity.notFound().build();
	}


//despues de cambiar manda el objeto sin moidficar  su identificador	
	public ResponseEntity<Object> actualizarComprobante(ComprobantedePago comprobanteNuevo) {
	   
	    for (ComprobantedePago comprobanteExistente : lista) {
	        if (comprobanteExistente.getProducto().getNombre().equals(comprobanteNuevo.getProducto().getNombre())){
	         
	            Producto productoEnComprobante = productoRepo.findById(comprobanteExistente.getProducto().getNombre()).orElse(null);
	            //solo la cantidad podria ser //igual busco pal stock no debria pero igual x la lista 
	            if (productoEnComprobante == null) {
	                return ResponseEntity.notFound().build();
	             }
	            
	            productoEnComprobante.setStock(productoEnComprobante.getStock() + comprobanteExistente.getCantidad()); 
	            productoRepo.save(productoEnComprobante);

	           
	            if (productoEnComprobante.getStock() >= comprobanteNuevo.getCantidad()) {
	                comprobanteExistente.setPrecioUnitario(productoEnComprobante.getPrecio());
	                comprobanteExistente.setPrecioTotal(productoEnComprobante.getPrecio() * comprobanteNuevo.getCantidad());

	                productoEnComprobante.setStock(productoEnComprobante.getStock() - comprobanteNuevo.getCantidad());
	                productoRepo.save(productoEnComprobante);
	                
	              //aca es de la lista se setea de ese objeto xk eso no va ala base de datos 
	                comprobanteExistente.setCantidad(comprobanteNuevo.getCantidad());
	                comprobanteExistente.setPrecioTotal(comprobanteNuevo.getPrecioTotal());

	                return ResponseEntity.ok("Producto actualizado correctamente en el carrito.");
	            } else {
	                // Restaurar el stock si no hay suficiente para la nueva cantidad
	                productoEnComprobante.setStock(productoEnComprobante.getStock() - comprobanteExistente.getCantidad());
	                productoRepo.save(productoEnComprobante);

	                return ResponseEntity.badRequest().body("El stock disponible es insuficiente para la nueva cantidad.");
	            }
	        }
	    }
	    return ResponseEntity.notFound().build(); 
	}

	
	//para cuando actulizes y elimines traes el objeto para mostar
	public ResponseEntity<Object> filtradoproductoporcarrito(String nomberProducto) {
	    for (ComprobantedePago comprobante : lista) {
	        if (comprobante.getProducto().getNombre().equals(nomberProducto)) {
	            return ResponseEntity.ok(comprobante);
	        }
	    }
	    return ResponseEntity.notFound().build();
	}

	
	public ResponseEntity<Object> vaciarCarrito() {
		
	   if (lista.isEmpty())
	   {
		   return ResponseEntity.noContent().build();
	   }
	   
	   for(ComprobantedePago comprobante:lista)
	   {
		   Producto producto=productoRepo.findById(comprobante.getProducto().getNombre()).orElse(null);
		   producto.setStock(producto.getStock()+comprobante.getCantidad());
		   productoRepo.save(producto);
		   //lomipiabas antes de recorratodo lista.clear();
	   }
	   lista.clear();
	   
	   return ResponseEntity.ok("Carrito Vacio");
	}

	

	
	public ResponseEntity<Object>buscarpedidoporcorreo(String correo)
	{
		List<Pedido>pedidosdelusuario=pedidoRepo.findByTarjeta_Cliente_Correo(correo);
		if(pedidosdelusuario.isEmpty())
		{
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(pedidosdelusuario);
		
	}
	
	
	public ResponseEntity<Object> guardarPedido(String correo) {
	    try {
	        if (lista.isEmpty()) {
	            return ResponseEntity.noContent().build();
	        }
	        Pedido pedido = new Pedido();
	        pedido.setFecha(LocalDate.now());
	        pedido.setEstado("Comprado");

	        // Buscar la tarjeta asociada al correo del cliente
	        Tarjeta tarjeta = tarjetaRepo.findByCliente_Correo(correo).orElse(null);
	        if (tarjeta == null) {
	            return ResponseEntity.notFound().build();
	        }
	        pedido.setTarjeta(tarjeta);

	        for (ComprobantedePago comprobante : lista) {
	            comprobante.setPedido(pedido);  // Asociar cada comprobante al pedido
	            pedido.getDetalles().add(comprobante);
	        }
	        
	        double totalGeneral = 0.0;
	        StringBuilder cuerpo = new StringBuilder();
	        
	       
	        pedidoRepo.save(pedido);  //guarda y pedido tien todo los artibutos
	        cuerpo.append("Detalles del Pedido #" + pedido.getIdPedido() + "\n\n");

	        for (ComprobantedePago comprobante : lista) {
	            cuerpo.append("- Producto: " + comprobante.getProducto().getNombre() + "\n");
	            cuerpo.append("  Cantidad: " + comprobante.getCantidad() + "\n");
	            cuerpo.append("  Precio Unitario: S/." + comprobante.getPrecioUnitario() + "\n");
	            cuerpo.append("  Precio Total: S/." + comprobante.getPrecioTotal() + "\n\n");

	            totalGeneral += comprobante.getPrecioTotal(); 
	        }

	        cuerpo.append("TOTAL GENERAL: S/." + totalGeneral + "\n");
	        cuerpo.append("Tarjeta asociada: " + tarjeta.getNumero() + "\n");
	        
	        //aca se envia al gmail
	        emailService.enviarCorreo(correo, "Tu pedido " + pedido.getIdPedido(), cuerpo.toString());

	        lista.clear();

	        return ResponseEntity.ok("Pedido guardado exitosamente y correo enviado.");
	    } catch (Exception e) {
	        e.printStackTrace();  
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Hubo un error al procesar el pedido. Por favor, intenta nuevamente.");
	    }
	}
	
	
	// Mostrar todos los productos agregados al carrito 
	public ResponseEntity<Object> visualizarCarrito() {
	    if (lista.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }
	    return ResponseEntity.ok(lista);
	}



	
	
	

}
