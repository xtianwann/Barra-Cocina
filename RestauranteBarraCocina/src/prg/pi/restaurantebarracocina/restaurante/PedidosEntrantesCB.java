
package prg.pi.restaurantebarracocina.restaurante;
/**
 * 
 * Clase que contiene toda la información sobre pedidos entrantes.
 * 
 * @author Juan G. Pérez Leo
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
	/**
     * 
     * Constructor:
     * 
     * @param nombreSeccion [String] Nombre de la sección asociada al pedido.
     * @param nombreMesa [String] Nombre de la mesa asociada al pedido.
     * @param idComanda [int] Id de la comanda asociada al pedido.
     * @param producto [Producto] Producto asociado al pedido.
     * @param unidades [int] Unidades del producto asociado al pedido.
     * @param listos [int] Unidades listas del producto asociado al pedido.
     */
	public PedidosEntrantesCB(String nombreSeccion, String nombreMesa, int idComanda, int unidades, Producto producto, int listos) {
		this.nombreSeccion = nombreSeccion;
		this.nombreMesa = nombreMesa;
		this.idComanda = idComanda;
		this.unidades = unidades;
		this.producto = producto;
		this.listos = listos;
		this.servido = false;
	}
	/**
     * 
     * Constructor:
     * 
     * @param nombreSeccion [String] Nombre de la sección asociada al pedido.
     * @param nombreMesa [String] Nombre de la mesa asociada al pedido.
     * @param idComanda [int] Id de la comanda asociada al pedido.
     * @param producto [Producto] Producto asociado al pedido.
     * @param unidades [int] Unidades del producto asociado al pedido.
     * @param listos [int] Unidades listas del producto asociado al pedido.
     * @param servidos [int] Unidades servidas del producto asociado al pedido.
     */
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
	/**
     * Permite obtener el nombre de la sección.
     * 
     * @return [String] Nombre de la sección.
     */
	public String getNombreSeccion() {
		return nombreSeccion;
	}
	/**
     * Permite modificar el nombre de la sección.
     * 
     * @param nombreSeccion [String] Nombre de la sección a modificar.
     */
	public void setNombreSeccion(String nombreSeccion) {
		this.nombreSeccion = nombreSeccion;
	}
	/**
     * Permite obtener el nombre de la mesa.
     * 
     * @return [String] Nombre de la mesa.
     */
	public String getNombreMesa() {
		return nombreMesa;
	}
	/**
     * Permite modificar el nombre de la mesa.
     * 
     * @param nombreMesa [String] Nombre de la mesa a modificar.
     */
	public void setNombreMesa(String nombreMesa) {
		this.nombreMesa = nombreMesa;
	}
	/**
     * Permite obtener el id de la comanda.
     * 
     * @return [int] Id de la comanda.
     */
	public int getIdComanda() {
		return idComanda;
	}
	/**
     * Permite modificar el id de la comanda.
     * 
     * @param idComanda [int] Id de la comanda a modificar.
     */
	public void setIdComanda(int idComanda) {
		this.idComanda = idComanda;
	}
	/**
     * Permite obtener el producto del pedido.
     * 
     * @return [Producto] Producto del pedido.
     */
	public Producto getProducto() {
		return producto;
	}
	/**
     * Permite modificar el producto.
     * 
     * @param producto [Producto] Producto a modificar.
     */
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	/**
     * Permite obtener la cantidad de pedidos listos.
     * 
     * @return [int] Cantidad pedidos listos.
     */
	public int getListos() {
		return listos;
	}
	/**
     * Permite modificar la cantidad de pedidos listos.
     * 
     * @param listos [int] Número de pedidos listos a modificar.
     */
	public void setListos(int listos) {
		this.listos = listos;
	}
	/**
     * Permite obtener la cantidad de pedidos.
     * 
     * @return [int] Cantidad pedidos.
     */
	public int getUnidades() {
		return unidades;
	}
	/**
     * Permite modificar la cantidad de pedidos.
     * 
     * @param unidades [int] Cantidad de pedidos a modificar.
     */
	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
	/**
     * Devuelve si hay mas pedidos listos que unidades
     * 
     * @return [boolean] true si existen mas listos que unidades y false en caso contrario.
     */
	public boolean isTerminado(){
		return listos >= unidades;
	}
	/**
     * Devuelve si el pedido esta totalmente servido.
     * 
     * @return [boolean] true si el pedido esta totalmente servido y false en caso contrario.
     */
	public boolean isServido() {
		return servido;
	}
	/**
     * Permite modificar el estado del pedido servido.
     * 
     * @param servido [boolean] Estado del pedido servido.
     */
	public void setServido(boolean servido) {
		this.servido = servido;
	}
	
}
