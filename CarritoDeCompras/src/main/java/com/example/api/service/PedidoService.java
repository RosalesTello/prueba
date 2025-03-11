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

  //seagrega un precio que no va con el produco seria error que sea automatico se sette no mandar 
    public ResponseEntity<Object> agregarProductoALista(ListaDeProductos pedidoTemporal) {
       
    	
        Producto productoenListadeProductos = productoRepositorio.findById(pedidoTemporal.getProducto().getNombre()).orElse(null);
        if (productoenListadeProductos==null) {
            return ResponseEntity.badRequest().body("El producto '" + pedidoTemporal.getProducto().getNombre() + "'no existe.");
        }
        
        if (productoenListadeProductos.getStock() >=pedidoTemporal.getCantidad()) {
        	pedidoTemporal.setPrecioUnitario(productoenListadeProductos.getPrecio());
        	pedidoTemporal.setPrecioTotal(pedidoTemporal.getPrecioUnitario()*pedidoTemporal.getCantidad());
        	productoenListadeProductos.setStock(productoenListadeProductos.getStock()-pedidoTemporal.getCantidad());
        	productoRepositorio.save(productoenListadeProductos);
            listaTemporal.add(pedidoTemporal);
        	return ResponseEntity.ok("Producto '" + productoenListadeProductos.getNombre() + "' agregado con total: " + pedidoTemporal.getPrecioTotal()+listaTemporal);
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
        
        //la lista tiene el nombre y la cantidad deberia llamarse PedidoTemporal
        //se busca el nombre de ese producto de esa lista  con el pedido 
        
        for (ListaDeProductos pedidoagregar : listaTemporal) {
            Producto producto = productoRepositorio.findById(pedidoagregar.getProducto().getNombre()).orElse(null);
            if (producto != null) {
                producto.setStock(producto.getStock() - pedidoagregar.getCantidad());
                productoRepositorio.save(producto);//se guarda el producto el stock
                pedidoagregar.setPedido(pedido);//Asociar ese pedido temporal ocn el pedido  sin save 
            }
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


}
