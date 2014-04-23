package prg.pi.restaurantebarracocina.restaurante;


public class Pedido {
	private Producto producto;
	private int unidades;
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public int getUnidades() {
		return unidades;
	}
	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
	public Pedido(Producto producto, int unidades) {
		super();
		this.producto = producto;
		this.unidades = unidades;
	}

}
