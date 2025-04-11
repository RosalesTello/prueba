package com.example.demo.entidades;

public class Producto {

	 private String nombre;
	    private double precio;
	    private int stock;
	    private String estado;
	    private String foto;
	    private Categoria categoria;

    // Constructor con parámetros
    public Producto(String nombre, double precio, int stock, String estado, String foto, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
        this.foto = foto;
        this.categoria = categoria;
    }

    // Constructor sin parámetros
    public Producto() {}

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
