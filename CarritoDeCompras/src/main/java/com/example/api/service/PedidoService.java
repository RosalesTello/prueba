package com.example.api.service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.api.entidades.ListaDeProductos;
import com.example.api.entidades.Pedido;
import com.example.api.entidades.Producto;
import com.example.api.repositorio.PedidoRepositorio;
import com.example.api.repositorio.ProductoRepositorio;

@Service
public class PedidoService {
	

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    private final List<ListaDeProductos> listaTemporal = new ArrayList<>();

 
    public ResponseEntity<Object> agregarProductoALista(ListaDeProductos pedidoTemporal) {
       
    	double total=0 ;
        Producto productoenListadeProductos = productoRepositorio.findById(pedidoTemporal.getProducto().getNombre()).orElse(null);
        if (productoenListadeProductos==null) {
            return ResponseEntity.badRequest().body("El producto '" + pedidoTemporal.getProducto().getNombre() + "'no existe.");
        }
        
        if (productoenListadeProductos.getStock() >=pedidoTemporal.getCantidad()) {
        	pedidoTemporal.setPrecioUnitario(productoenListadeProductos.getPrecio());
        	pedidoTemporal.setPrecioTotal(productoenListadeProductos.getPrecio()*pedidoTemporal.getCantidad());
        	
        	productoenListadeProductos.setStock(productoenListadeProductos.getStock()-pedidoTemporal.getCantidad());
        	productoRepositorio.save(productoenListadeProductos);
            listaTemporal.add(pedidoTemporal);
            
            //para mostart el total de lo que se va agregando 
            for (ListaDeProductos i :listaTemporal)
            {
            	total+=i.getPrecioTotal();
            }
            
        	return ResponseEntity.ok(listaTemporal+ "precio total a pagar es de:" +total);
        	//no se debe descontar aun el stock de productos xk no va ir ala base de datos 
        }
        return ResponseEntity.badRequest().body("Stock insuficiente para '" + productoenListadeProductos.getNombre() + "'."); 
        
    }
    
    
    public ResponseEntity<Object> guardarPedido() {
        if (listaTemporal.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay productos en la lista para guardar el pedido.");
        }
        
        Pedido pedido = new Pedido();
        pedido.setItems(new ArrayList<>(listaTemporal));
        LocalDate fechaActual = LocalDate.now();
        pedido.setFecha(fechaActual);
        
        for (ListaDeProductos pedidoagregar : listaTemporal) {
           
                pedidoagregar.setPedido(pedido);//Asociar cada producto temporal ocn el pedido  sin save 
            }
               
        pedidoRepositorio.save(pedido);
        listaTemporal.clear();
        return ResponseEntity.ok("Pedido guardado exitosamente.");
    }
    
    
    public ResponseEntity<Object>filtrado(int id)
    {
    	Pedido pedidoExistente=pedidoRepositorio.findById(id).orElse(null);
    	
    	if(pedidoExistente==null)
    	{
    		return ResponseEntity.notFound().build();
    	}
    	return ResponseEntity.ok(pedidoExistente);
    }
    
    
    
    public ResponseEntity<Object> vaciarCarrito() {
    	
    	if (listaTemporal.isEmpty())
    	{
    		return ResponseEntity.noContent().build();
    	}
    	
        for (ListaDeProductos item : listaTemporal) {
        	//item.getProducto().setStock(14-1);
        	//productoRepositorio.save(item); gurdas un objeto item no producto no deja
        	//no puedes setiar el stock del otro 
            Producto producto = productoRepositorio.findById(item.getProducto().getNombre()).orElse(null);
             producto.setStock(producto.getStock() + item.getCantidad());
             productoRepositorio.save(producto);
        }
        listaTemporal.clear();
        return ResponseEntity.ok("Carrito vaciado y stock restaurado correctamente.");
    }
    
    
    
    
    
    //aca actualizar a probar mañana 
    public ResponseEntity<Object> actualizarPedido(int id, Pedido nuevoPedido) {
        Pedido pedidoExistente = pedidoRepositorio.findById(id).orElse(null);

        if (pedidoExistente == null) {
            return ResponseEntity.notFound().build();
        }

        // 🔹 1. Devolver el stock de los productos del pedido actual antes de modificarlo
        for (ListaDeProductos item : pedidoExistente.getItems()) {
            Producto producto = productoRepositorio.findById(item.getProducto().getNombre()).orElse(null);
            if (producto != null) {
                producto.setStock(producto.getStock() + item.getCantidad()); // Devuelve el stock original
                productoRepositorio.save(producto);
            }
        }

        // 🔹 2. Validar stock antes de actualizar
        for (ListaDeProductos nuevoItem : nuevoPedido.getItems()) {
            Producto producto = productoRepositorio.findById(nuevoItem.getProducto().getNombre()).orElse(null);

            if (producto == null) {
                return ResponseEntity.badRequest().body("El producto '" + nuevoItem.getProducto().getNombre() + "' no existe.");
            }

            if (producto.getStock() >= nuevoItem.getCantidad()) {
                producto.setStock(producto.getStock() - nuevoItem.getCantidad()); // Reduce stock según el nuevo pedido
                productoRepositorio.save(producto);
            } else {
                return ResponseEntity.badRequest().body("Stock insuficiente para '" + producto.getNombre() + "'. Disponible: " + producto.getStock());
            }
        }

        // 🔹 3. Actualizar el pedido en la base de datos
        pedidoExistente.setFecha(nuevoPedido.getFecha());
        pedidoExistente.setItems(nuevoPedido.getItems());

        pedidoRepositorio.save(pedidoExistente);
        return ResponseEntity.ok("Pedido actualizado correctamente.");
    }



    

}
