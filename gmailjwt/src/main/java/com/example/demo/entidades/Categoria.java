package com.example.demo.entidades;

public class Categoria {
    
    private String nombreCategoria;
    private String estado;

    
    public Categoria(String nombreCategoria, String estado) {
        this.nombreCategoria = nombreCategoria;
        this.estado = estado;
    }

    // Constructor sin parámetros
    public Categoria() {}

    // Getters y setters
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
