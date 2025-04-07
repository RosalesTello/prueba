package com.example.api.Entidades;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Cliente {
    
    @Id
    private String nombreCliente; // PK
    private String correo;
    private String estado;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
   
    private List<Tarjeta> tarjetas;

    public Cliente(String nombreCliente, String correo, String estado, List<Tarjeta> tarjetas) {
        super();
        this.nombreCliente = nombreCliente;
        this.correo = correo;
        this.estado = estado;
        this.tarjetas = tarjetas;
    }

    public Cliente() {
        super();
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Tarjeta> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<Tarjeta> tarjetas) {
        this.tarjetas = tarjetas;
    }
}

