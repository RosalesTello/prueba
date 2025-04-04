package com.example.api.Respositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.Entidades.Pedido;
@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Integer>{
	
	List<Pedido>findByTarjeta_Cliente_Correo(String correo);
	

}
