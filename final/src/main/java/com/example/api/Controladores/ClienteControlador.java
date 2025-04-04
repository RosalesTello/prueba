package com.example.api.Controladores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.api.Entidades.Cliente;
import com.example.api.Sericio.ClienteServicio;

@RequestMapping("Clientes")
@RestController
public class ClienteControlador {
	
	 @Autowired ClienteServicio clienteServicio;
	    
	    @GetMapping("/listar")
	    public ResponseEntity<Object> listarClientes() {
	        return clienteServicio.listarClientes();
	    }
	    
	    @PostMapping("/agregar")
	    public ResponseEntity<Object> agregarCliente(@RequestBody Cliente cliente) {
	        return clienteServicio.agregarCliente(cliente);
	    }
	    
	    @PutMapping("/editar")
	    public ResponseEntity<Object> editarCliente(@RequestBody Cliente cliente) {
	        return clienteServicio.editarCliente(cliente);
	    }

}
