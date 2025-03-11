package com.example.api.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ListaDeProductos {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idItem;

    @ManyToOne
    @JoinColumn(name = "idPedido", referencedColumnName="idPedido")
    @JsonBackReference
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "nombre_Producto", referencedColumnName = "nombre")
    private Producto producto;

    private int cantidad;
    private double precioUnitario;
    private double precioTotal;
	public ListaDeProductos(int idItem, Pedido pedido, Producto producto, int cantidad, double precioUnitario,
			double precioTotal) {
		super();
		this.idItem = idItem;
		this.pedido = pedido;
		this.producto = producto;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.precioTotal = precioTotal;
	}
	public ListaDeProductos() {
		super();
	}
	public int getIdItem() {
		return idItem;
	}
	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}
	public Pedido getPedido() {
		return pedido;
	}
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public double getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	public double getPrecioTotal() {
		return precioTotal;
	}
	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
	}
    
    

}
