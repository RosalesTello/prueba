package com.example.api.Respositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.Entidades.ComprobantedePago;
@Repository

public interface ComprobantedePagoRepositorio extends JpaRepository<ComprobantedePago, Integer>{

}
