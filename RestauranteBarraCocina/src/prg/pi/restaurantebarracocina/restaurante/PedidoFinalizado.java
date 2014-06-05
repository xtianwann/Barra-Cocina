package prg.pi.restaurantebarracocina.restaurante;
/**
 * 
 * Clase que contiene toda la información sobre pedidos finalizados.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class PedidoFinalizado {
	
	private int idComanda;
	private int idMenu;
	/**
     * 
     * Constructor:
     * 
     * @param idComanda [int] Id de la comanda del pedido que ha finalizado.
     * @param idMenu [int] Id de menu del pedido que ha finalizado.
     */
	public PedidoFinalizado(int idComanda, int idMenu){
		this.idComanda = idComanda;
		this.idMenu = idMenu;
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
     * Permite obtener el id del menu.
     * 
     * @return [int] Id del menu.
     */
	public int getIdMenu() {
		return idMenu;
	}

}
