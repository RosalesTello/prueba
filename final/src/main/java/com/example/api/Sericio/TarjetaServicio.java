package com.example.api.Sericio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.api.Entidades.Cliente;
import com.example.api.Entidades.Tarjeta;
import com.example.api.Respositorios.ClienteRepositorio;
import com.example.api.Respositorios.TarjetaRepositorio;

@Service
public class TarjetaServicio {
	
	 @Autowired
	    private TarjetaRepositorio tarjetaRepo;
	 
	 @Autowired ClienteRepositorio clienteRepo;

	    public ResponseEntity<Object> listarTarjetas() {
	        List<Tarjeta> tarjetas = tarjetaRepo.findAll();
	        if (tarjetas.isEmpty()) {
	            return ResponseEntity.noContent().build();
	        }
	        return ResponseEntity.ok(tarjetas);
	    }
	    
	    //solopara gregar tarjeta tarjeta no repite usuario si  cliente
	    public ResponseEntity<Object> agregarTarjeta(Tarjeta tarjeta,String correo) {
	    	//buscodefrente en cliente podria ser con nombre
	    	Cliente cliente=clienteRepo.findByCorreo(correo).orElse(null);
		    if (cliente==null)
		     {
		        return ResponseEntity.notFound().build();
		     } 
	    	
	    	Tarjeta tarjetarAgregar=tarjetaRepo.findById(tarjeta.getNumero()).orElse(null);
	        if (tarjetarAgregar==null) {
	        	
	        	tarjeta.setEstado("Activo");
	        	tarjeta.setTipo("BCP");
	        	tarjeta.setCliente(cliente); // Asignamos el cliente encontrado a la tarjeta.
	            tarjetaRepo.save(tarjeta); // Guardamos la tarjeta en el repositorio.
	           
	            return ResponseEntity.status(HttpStatus.CREATED).build();
	        }
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("La tarjeta Ya esxiste");
	    }
	    
	   //no cambia el nombre ni el numero de la tarjeta
	    public ResponseEntity<Object> editarTarjeta(Tarjeta tarjetaNueva) {
	        Tarjeta tarjetaExistente = tarjetaRepo.findById(tarjetaNueva.getNumero()).orElse(null);
	        if (tarjetaExistente != null) {
	            tarjetaExistente.setTipo(tarjetaNueva.getTipo());
	            tarjetaExistente.setFechaExpiracion(tarjetaNueva.getFechaExpiracion());
	            tarjetaExistente.setEstado(tarjetaNueva.getEstado());
	            tarjetaRepo.save(tarjetaExistente);
	            return ResponseEntity.ok("Tarjeta actualizada correctamente");
	        }
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarjeta no encontrada");
	    }

}
