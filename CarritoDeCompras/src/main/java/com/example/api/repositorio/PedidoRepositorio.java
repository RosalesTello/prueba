package com.example.api.repositorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.api.entidades.Pedido;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Integer> {

}
