package com.example.demo.entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
	
	private int idPedido;
    private LocalDate fecha;
    private String estado;
    private List<ComprobantedePago> detalles = new ArrayList<>();
    private Tarjeta tarjeta;
	public Pedido() {
		super();
	}
	public Pedido(int idPedido, LocalDate fecha, String estado, List<ComprobantedePago> detalles, Tarjeta tarjeta) {
		super();
		this.idPedido = idPedido;
		this.fecha = fecha;
		this.estado = estado;
		this.detalles = detalles;
		this.tarjeta = tarjeta;
	}
	public int getIdPedido() {
		return idPedido;
	}
	public void setIdPedido(int idPedido) {
		this.idPedido = idPedido;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public List<ComprobantedePago> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<ComprobantedePago> detalles) {
		this.detalles = detalles;
	}
	public Tarjeta getTarjeta() {
		return tarjeta;
	}
	public void setTarjeta(Tarjeta tarjeta) {
		this.tarjeta = tarjeta;
	}
    
    
	
    

}
