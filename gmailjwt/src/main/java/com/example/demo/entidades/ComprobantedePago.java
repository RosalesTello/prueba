package com.example.demo.entidades;

public class ComprobantedePago {
    

	 private int idComprobantedePago;
	    private String estado;
	    private int cantidad;
	    private double precioUnitario;  // Nota: nombres de variables en camelCase
	    private double precioTotal;     // Nota: nombres de variables en camelCase
	    private Pedido pedido;
	    private Producto producto;
		public ComprobantedePago() {
			super();
		}
		public ComprobantedePago(int idComprobantedePago, String estado, int cantidad, double precioUnitario,
				double precioTotal, Pedido pedido, Producto producto) {
			super();
			this.idComprobantedePago = idComprobantedePago;
			this.estado = estado;
			this.cantidad = cantidad;
			this.precioUnitario = precioUnitario;
			this.precioTotal = precioTotal;
			this.pedido = pedido;
			this.producto = producto;
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
