package prg.pi.restaurantebarracocina.restaurante;

public class PedidoActualizado {
	
	private int idMenu;
	private int idComanda;
	private String nombreProducto;
	private String nombreCantidad;
	private int unidades;
	private int cantidadPedidos;
	private int cantidadListos;
	private int cantidadServidos;
	private String nombreSeccion;
	private String nombreMesa;
	
	public PedidoActualizado(String nombreSeccion,String nombreMesa,int idMenu, int idComanda, String nombreProducto, String nombreCantidad, int unidades, int cantidadPedidos, int cantidadListos, int cantidadServidos){
		this.idMenu = idMenu;
		this.idComanda = idComanda;
		this.nombreSeccion = nombreSeccion;
		this.nombreMesa = nombreMesa;
		this.nombreProducto = nombreProducto;
		this.nombreCantidad = nombreCantidad;
		this.unidades = unidades;
		this.cantidadPedidos = cantidadPedidos;
		this.cantidadListos = cantidadListos;
		this.cantidadServidos = cantidadServidos;
	}

	public int getIdMenu() {
		return idMenu;
	}

	public int getIdComanda() {
		return idComanda;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public String getNombreCantidad() {
		return nombreCantidad;
	}

	public int getUnidades() {
		return unidades;
	}

	public int getCantidadPedidos() {
		return cantidadPedidos;
	}

	public int getCantidadListos() {
		return cantidadListos;
	}

	public int getCantidadServidos() {
		return cantidadServidos;
	}

	public String getNombreSeccion() {
		return nombreSeccion;
	}

	public String getNombreMesa() {
		return nombreMesa;
	}
}
