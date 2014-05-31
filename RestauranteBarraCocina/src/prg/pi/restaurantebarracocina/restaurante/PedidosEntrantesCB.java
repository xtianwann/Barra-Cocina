
package prg.pi.restaurantebarracocina.restaurante;

/**
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class PedidosEntrantesCB {

	private String nombreSeccion;
	private String nombreMesa;
	private int idComanda;
	private int unidades;
	private Producto producto;
	private int listos;
	private boolean servido;

	public PedidosEntrantesCB(String nombreSeccion, String nombreMesa, int idComanda, int unidades, Producto producto, int listos) {
		this.nombreSeccion = nombreSeccion;
		this.nombreMesa = nombreMesa;
		this.idComanda = idComanda;
		this.unidades = unidades;
		this.producto = producto;
		this.listos = listos;
		this.servido = false;
	}
	
	public PedidosEntrantesCB(String nombreSeccion, String nombreMesa, int idComanda, int unidades, Producto producto, int listos, int servidos) {
		this.nombreSeccion = nombreSeccion;
		this.nombreMesa = nombreMesa;
		this.idComanda = idComanda;
		this.unidades = unidades;
		this.producto = producto;
		this.listos = listos;
		if(servidos == unidades)
			this.servido = true;
		else
			this.servido = false;
	}

	public String getNombreSeccion() {
		return nombreSeccion;
	}

	public void setNombreSeccion(String nombreSeccion) {
		this.nombreSeccion = nombreSeccion;
	}

	public String getNombreMesa() {
		return nombreMesa;
	}

	public void setNombreMesa(String nombreMesa) {
		this.nombreMesa = nombreMesa;
	}

	public int getIdComanda() {
		return idComanda;
	}

	public void setIdComanda(int idComanda) {
		this.idComanda = idComanda;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getListos() {
		return listos;
	}

	public void setListos(int listos) {
		this.listos = listos;
	}

	public int getUnidades() {
		return unidades;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
	
	public boolean isTerminado(){
		return listos >= unidades;
	}
	
	public boolean isServido() {
		return servido;
	}

	public void setServido(boolean servido) {
		this.servido = servido;
	}
	
}
