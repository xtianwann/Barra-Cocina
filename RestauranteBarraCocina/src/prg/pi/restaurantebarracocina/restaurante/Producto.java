package prg.pi.restaurantebarracocina.restaurante;

public class Producto {
	private int idMenu;
	private String nombreProducto;
	private String cantidadPadre;
	
	public Producto(int idMenu, String nombreProducto) {
		this.idMenu = idMenu;
		this.nombreProducto = nombreProducto;
	}

	public Producto(int idMenu, String nombreProducto, String cantidadPadre) {
		this.idMenu = idMenu;
		this.nombreProducto = nombreProducto;
		this.cantidadPadre = cantidadPadre;
	}
	
	public String getCantidadPadre() {
		return cantidadPadre;
	}
	
	public void setCantidadPadre(String cantidadPadre) {
		this.cantidadPadre = cantidadPadre;
	}
	
	public int getIdMenu() {
		return idMenu;
	}
	
	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
	}
	
	public String getNombreProducto() {
		return nombreProducto;
	}
	
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}
	
}
