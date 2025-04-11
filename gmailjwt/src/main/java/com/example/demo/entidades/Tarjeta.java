package com.example.demo.entidades;

public class Tarjeta {
	private String numero;
    private String tipo;
    private String fechaExpiracion;
    private String estado;
    private double montoDisponible;
	public Tarjeta(String numero, String tipo, String fechaExpiracion, String estado, double montoDisponible) {
		super();
		this.numero = numero;
		this.tipo = tipo;
		this.fechaExpiracion = fechaExpiracion;
		this.estado = estado;
		this.montoDisponible = montoDisponible;
	}
	public Tarjeta() {
		super();
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getFechaExpiracion() {
		return fechaExpiracion;
	}
	public void setFechaExpiracion(String fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public double getMontoDisponible() {
		return montoDisponible;
	}
	public void setMontoDisponible(double montoDisponible) {
		this.montoDisponible = montoDisponible;
	}
    

}
