package prg.pi.restaurantebarracocina.restaurante;

/**
 * Clase que contiene toda la información sobre pedidos modificados por el camarero.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class PedidoModificadoCamarero {
	
	private int idComanda;
	private int idMenu;
	private int unidades;
	private boolean todosServidos; 
	
	/**
     * Constructor:
     * 
     * @param idComanda [int] Id de la comanda del pedido que se ha modificado.
     * @param idMenu [int] Id de menu del pedido que se ha modificado.
     * @param unidades [int] Unidades a modificar.
     */
	public PedidoModificadoCamarero(int idComanda, int idMenu, int unidades){
		this.idComanda = idComanda;
		this.idMenu = idMenu;
		this.unidades = unidades;
		this.todosServidos = false;
	}
	
	/**
     * Constructor:
     * 
     * @param idComanda [int] Id de la comanda del pedido que se ha modificado.
     * @param idMenu [int] Id de menu del pedido que se ha modificado.
     * @param unidades [int] Unidades a modificar.
     * @param todosServidos [boolean] Pedido totalmente servido.
     */
	public PedidoModificadoCamarero(int idComanda, int idMenu, int unidades, boolean todosServidos){
		this.idComanda = idComanda;
		this.idMenu = idMenu;
		this.unidades = unidades;
		this.todosServidos = todosServidos;
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
     * Permite modificar el id de la comanda del pedido.
     * 
     * @param idComanda [int] Id de la comanda.
     */
	public void setIdComanda(int idComanda) {
		this.idComanda = idComanda;
	}
	
	/**
     * Permite obtener el id del menu.
     * 
     * @return [int] Id del menu.
     */
	public int getIdMenu() {
		return idMenu;
	}
	
	/**
     * Permite modificar el id del menu del pedido.
     * 
     * @param idMenu [int] Id del menu.
     */
	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
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
     * @param unidades [int] Unidades de pedido.
     */
	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
	
	/**
     * Permite obtener si el pedido esta totalmente servido.
     * 
     * @return [boolean] true si el pedido esta totalmente servido, false en caso contrario.
     */
	public boolean isTodosServidos() {
		return todosServidos;
	}
	
	/**
     * Permite modificar el pedido totalmente servido.
     * 
     * @param todosServidos [boolean] Pedido totalmente servido.
     */
	public void setTodosServidos(boolean todosServidos) {
		this.todosServidos = todosServidos;
	}
}
