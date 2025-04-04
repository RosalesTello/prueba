package com.example.api.Sericio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
	public ResponseEntity<Object> eliminar(int idComprobante) {

	    // Buscar el comprobante por id
	    for (ComprobantedePago comprobante : lista) {
	        if (comprobante.getIdComprobantedePago() == idComprobante) {
	            Producto producto=productoRepo.findById(comprobante.getProducto().getNombre()).orElse(null);
	            producto.setStock( producto.getStock()+comprobante.getCantidad());
	            lista.remove(comprobante);
	            return ResponseEntity.ok("Producto Eliminado del Carrito");
	        }
	    }

	    return ResponseEntity.notFound().build();
	}

//despues de cambiar manda el objeto sin moidficar  su identificador	
	public ResponseEntity<Object> actualizarComprobante(ComprobantedePago comprobanteActualizado) {
	   
	    for (ComprobantedePago comprobante : lista) {
	        if (comprobante.getIdComprobantedePago() == comprobanteActualizado.getIdComprobantedePago()) {
	         
	            Producto productoEnComprobante = productoRepo.findById(comprobante.getProducto().getNombre()).orElse(null);
	            //solo la cantidad podria ser //igual busco pal stock no debria pero igual x la lista 
	            if (productoEnComprobante == null) {
	                return ResponseEntity.notFound().build();
	             }
	            
	            productoEnComprobante.setStock(productoEnComprobante.getStock() + comprobante.getCantidad()); 
	            productoRepo.save(productoEnComprobante);

	           
	            if (productoEnComprobante.getStock() >= comprobanteActualizado.getCantidad()) {
	                comprobanteActualizado.setPrecioUnitario(productoEnComprobante.getPrecio());
	                comprobanteActualizado.setPrecioTotal(productoEnComprobante.getPrecio() * comprobanteActualizado.getCantidad());

	                productoEnComprobante.setStock(productoEnComprobante.getStock() - comprobanteActualizado.getCantidad());
	                productoRepo.save(productoEnComprobante);
	                
	              //aca es de la lista se setea de ese objeto xk eso no va ala base de datos 
	                comprobante.setCantidad(comprobanteActualizado.getCantidad());
	                comprobante.setPrecioTotal(comprobanteActualizado.getPrecioTotal());

	                return ResponseEntity.ok("Producto actualizado correctamente en el carrito.");
	            } else {
	                // Restaurar el stock si no hay suficiente para la nueva cantidad
	                productoEnComprobante.setStock(productoEnComprobante.getStock() - comprobante.getCantidad());
	                productoRepo.save(productoEnComprobante);

	                return ResponseEntity.badRequest().body("El stock disponible es insuficiente para la nueva cantidad.");
	            }
	        }
	    }
	    return ResponseEntity.notFound().build(); 
	}

	
	//para cuando actulizes y elimines traes el objeto para mostar
	public ResponseEntity<Object> filtradoproductoporcarrito(int idComprobantedePago) {
	    for (ComprobantedePago comprobante : lista) {
	        if (comprobante.getIdComprobantedePago() == idComprobantedePago) {
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
		   lista.clear();
	   }
	   
	   return ResponseEntity.ok("Carrito Vacio");
	}

	
	
	///podria llamar este metodo con parametro y aya ya lo paso ee parametro 
	public ResponseEntity<Object> guardarPedido(String correo) {
	    
	    if (lista.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }
	    Pedido pedido = new Pedido();
	    pedido.setFecha(LocalDate.now()); 
	    pedido.setEstado("Comprado"); 
	  //encuentra la tarjeta de ese usuario y cuado compra o pone
	    Tarjeta tarjeta=tarjetaRepo.findByCliente_Correo(correo).orElse(null);
	    if (tarjeta == null) {
	        return ResponseEntity.notFound().build();
	    }
	    pedido.setTarjeta(tarjeta);
	    
	    for (ComprobantedePago comprobante : lista) {
	        comprobante.setPedido(pedido);  // Asocia cada comprobante de pago con el pedido
	    }

	    lista.clear();
	 
	    return ResponseEntity.ok("Pedido guardado exitosamente.");
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

	
	
	

}
