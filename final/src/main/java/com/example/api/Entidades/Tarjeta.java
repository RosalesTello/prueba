package com.example.api.Entidades;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Tarjeta {
    
    @Id
    private String numero;
    private String tipo;
    private String fechaExpiracion;
    private String estado;
    private double montoDisponible;
    
    @ManyToOne
    @JoinColumn(name = "Cliente", referencedColumnName = "nombreCliente") // FK con ID
    
    
    private Cliente cliente;
    
  
    
    @OneToMany(mappedBy = "tarjeta",cascade = CascadeType.ALL, fetch = FetchType.LAZY)//tarejta en pedidos para que compre defrente
    @JsonManagedReference
    private List<Pedido>pedidos;



	public Tarjeta(String numero, String tipo, String fechaExpiracion, String estado, double montoDisponible,
			Cliente cliente, List<Pedido> pedidos) {
		super();
		this.numero = numero;
		this.tipo = tipo;
		this.fechaExpiracion = fechaExpiracion;
		this.estado = estado;
		this.montoDisponible = montoDisponible;
		this.cliente = cliente;
		this.pedidos = pedidos;
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



	public Cliente getCliente() {
		return cliente;
	}



	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}



	public List<Pedido> getPedidos() {
		return pedidos;
	}



	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}


    
}



