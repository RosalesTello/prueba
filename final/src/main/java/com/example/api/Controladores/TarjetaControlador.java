package com.example.api.Controladores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.api.Entidades.Tarjeta;
import com.example.api.Sericio.TarjetaServicio;

@RequestMapping("/Tarjetas")
@RestController()

public class TarjetaControlador {
	 	@Autowired
	    private TarjetaServicio tarjetaServicio;
	    
	    @GetMapping("/listar")
	    public ResponseEntity<Object> listarTarjetas() {
	        return tarjetaServicio.listarTarjetas();
	    }
	    
	    @PostMapping("/agregar")
	    public ResponseEntity<Object> agregarTarjeta(@RequestBody Tarjeta tarjeta,String correo) {
	        return tarjetaServicio.agregarTarjeta(tarjeta,correo);
	    }
	    
	    @PutMapping("/editar")
	    public ResponseEntity<Object> editarTarjeta(@RequestBody Tarjeta tarjeta) {
	        return tarjetaServicio.editarTarjeta(tarjeta);
	    }

}
