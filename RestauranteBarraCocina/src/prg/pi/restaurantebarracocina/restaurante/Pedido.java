package prg.pi.restaurantebarracocina.restaurante;
/**
 * 
 * Clase que contiene toda la informaci�n sobre pedidos.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class Pedido {
	private Producto producto;
	private int unidades;
	/**
     * 
     * Constructor:
     * 
     * @param producto [Producto] Producto a a�adir al pedido.
     * @param unidades [int] Unidades del producto a a�adir al pedido.
     */
	public Pedido(Producto producto, int unidades) {
		super();
		this.producto = producto;
		this.unidades = unidades;
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
     * Permite modificar el producto del pedido.
     * 
     * @param producto [Producto] Producto del pedido a modificar.
     */
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	/**
     * Permite obtener las unidades del pedido.
     * 
     * @return [int] Unidades del pedido.
     */
	public int getUnidades() {
		return unidades;
	}
	/**
     * Permite modificar las unidades del pedido.
     * 
     * @param unidades [int] Unidades del pedido a modificar.
     */
	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
}
