package com.example.api.Entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ComprobantedePago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idComprobantedePago;
    private String estado;
    private int cantidad;
    private double PrecioUnitario;
    private double PrecioTotal;
    
    @ManyToOne
    @JoinColumn(name = "Pedido", referencedColumnName = "idPedido") // FK correcta
    @JsonBackReference
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "Producto", referencedColumnName = "nombre") // FK correcta
    private Producto producto;

	public ComprobantedePago(int idComprobantedePago, String estado, int cantidad, double precioUnitario,
			double precioTotal, Pedido pedido, Producto producto) {
		super();
		this.idComprobantedePago = idComprobantedePago;
		this.estado = estado;
		this.cantidad = cantidad;
		PrecioUnitario = precioUnitario;
		PrecioTotal = precioTotal;
		this.pedido = pedido;
		this.producto = producto;
	}

	public ComprobantedePago() {
		super();
	}

	public int getIdComprobantedePago() {
		return idComprobantedePago;
	}

	public void setIdComprobantedePago(int idComprobantedePago) {
		this.idComprobantedePago = idComprobantedePago;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecioUnitario() {
		return PrecioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		PrecioUnitario = precioUnitario;
	}

	public double getPrecioTotal() {
		return PrecioTotal;
	}

	public void setPrecioTotal(double precioTotal) {
		PrecioTotal = precioTotal;
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
    
    
    
   
}


