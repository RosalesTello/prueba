package com.example.api.entidades;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Pedido {
	
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int idPedido;

	    private LocalDate fecha;
	    

	    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
	    @JsonManagedReference 
	    private List<ListaDeProductos> items;

		public Pedido(int idPedido, LocalDate fecha, List<ListaDeProductos> items) {
			super();
			this.idPedido = idPedido;
			this.fecha = fecha;
			this.items = items;
		}

		public Pedido() {
			super();
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

		public void setFecha(LocalDate fechaActual) {
			this.fecha = fechaActual;
		}

		public List<ListaDeProductos> getItems() {
			return items;
		}

		public void setItems(List<ListaDeProductos> items) {
			this.items = items;
		}
	    
	    

}
