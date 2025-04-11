package com.example.demo.entidades;


public class Cliente {
	 private String nombreCliente;
	    private String correo;
	    private String estado;
		public Cliente(String nombreCliente, String correo, String estado) {
			super();
			this.nombreCliente = nombreCliente;
			this.correo = correo;
			this.estado = estado;
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
	    

}
