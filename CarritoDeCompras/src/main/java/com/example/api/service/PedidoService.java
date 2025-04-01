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
        	productoRepositorio.save(productoenListadeProductos);//aca lo guarda en la abse de datos
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
            Producto producto = productoRepositorio.findById(item.getProducto().getNombre()).orElse(null);
             producto.setStock(producto.getStock() + item.getCantidad());
             productoRepositorio.save(producto);
        }
        listaTemporal.clear();
        return ResponseEntity.ok("Carrito vaciado y stock restaurado correctamente.");
    }
    
    
    public ResponseEntity<Object> eliminarProductoDelCarrito(String nombreProducto) {
        for (ListaDeProductos item : listaTemporal) {
        	//trae todo el objeto
            if (item.getProducto().getNombre().equals(nombreProducto)) {
                // Restaurar stock
                Producto producto = productoRepositorio.findById(nombreProducto).orElse(null);
                if (producto != null) {
                    producto.setStock(producto.getStock() + item.getCantidad());
                    productoRepositorio.save(producto);
                }
//s
                listaTemporal.remove(item);
                return ResponseEntity.ok("Producto '" + nombreProducto + "' eliminado del carrito y stock restaurado.");
            }
        }

        return ResponseEntity.badRequest().body("El producto no está en el carrito.");
    }
    
    
    public ResponseEntity<Object> obtenerCarrito() {
        if (listaTemporal.isEmpty()) {
            return ResponseEntity.ok("El carrito está vacío.");
        }
        return ResponseEntity.ok(listaTemporal);
    }
    
    
    public ResponseEntity<Object> actualizarProductoEnCarrito(ListaDeProductos productoActualizado) {
        for (ListaDeProductos item : listaTemporal) {
            if (item.getProducto().getNombre().equals(productoActualizado.getProducto().getNombre())) {
                Producto producto = productoRepositorio.findById(productoActualizado.getProducto().getNombre()).orElse(null);
                if (producto == null) {
                    return ResponseEntity.badRequest().body("El producto con nombre '" + productoActualizado.getProducto().getNombre() + "' no existe en la base de datos.");
                }
                

                producto.setStock(producto.getStock() + item.getCantidad()); 
                productoRepositorio.save(producto);

                if (producto.getStock() >= productoActualizado.getCantidad()) {
                   
                    producto.setStock(producto.getStock() - productoActualizado.getCantidad()); 
                    productoRepositorio.save(producto);

                    //aca es de la lista se setea de ese objeto
                    item.setCantidad(productoActualizado.getCantidad());
                    item.setPrecioTotal(item.getPrecioUnitario() * productoActualizado.getCantidad());

                    return ResponseEntity.ok("Producto '" + productoActualizado.getProducto().getNombre() + "' actualizado en el carrito.");
                } else {

                    producto.setStock(producto.getStock() - item.getCantidad());
                    productoRepositorio.save(producto);
                    return ResponseEntity.badRequest().body("El stock disponible es insuficiente para la nueva cantidad.");
                }
            }
        }

        return ResponseEntity.badRequest().body("El producto no se encuentra en el carrito.");
    }


    public ResponseEntity<Object>filtradoCarrito(String nombre)
    {
    	for (ListaDeProductos objeto:listaTemporal)
    	{
    		if(objeto.getProducto().getNombre().equals(nombre)) {
    		return ResponseEntity.ok(objeto);
    		}
    	}
    	return ResponseEntity.notFound().build();
    }
    
 

}
