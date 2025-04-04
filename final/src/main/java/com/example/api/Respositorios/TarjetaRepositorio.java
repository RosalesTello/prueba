package com.example.api.Respositorios;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.Entidades.Tarjeta;
@Repository
public interface TarjetaRepositorio extends JpaRepository<Tarjeta,String> {
	Optional<Tarjeta> findByCliente_Correo(String correo);//si hay mas ponle list por ahora con uno

}
