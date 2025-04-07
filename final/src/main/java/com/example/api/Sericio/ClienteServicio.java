package com.example.api.Sericio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.api.Entidades.Cliente;
import com.example.api.Respositorios.ClienteRepositorio;
@Service
public class ClienteServicio {
	
	 @Autowired ClienteRepositorio clienteRepo;

	    public ResponseEntity<Object> listarClientes() {
	        List<Cliente> clientes = clienteRepo.findAll();
	        if (clientes.isEmpty()) {
	            return ResponseEntity.noContent().build();
	        }
	        return ResponseEntity.ok(clientes);
	    }

	    public ResponseEntity<Object> agregarCliente(Cliente cliente) {
	    	
	    	Cliente clienteAgregar=clienteRepo.findByCorreo(cliente.getCorreo()).orElse(null);
	        if (clienteAgregar==null) {
	        	cliente.setEstado("Activo");
	        	clienteRepo.save(cliente);
	        	 return ResponseEntity.status(HttpStatus.CREATED).build();
	        }
	        
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("El cliente ya existe");
	    }

	    public ResponseEntity<Object> editarCliente(Cliente clienteNuevo) {
	        Cliente clienteExistente = clienteRepo.findById(clienteNuevo.getNombreCliente()).orElse(null);
	        if (clienteExistente != null) {
	            clienteExistente.setCorreo(clienteNuevo.getCorreo());
	            clienteExistente.setEstado(clienteNuevo.getEstado());
	            clienteRepo.save(clienteExistente);
	            return ResponseEntity.ok("Cliente actualizado correctamente");
	        }
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
	    }

}
