package prg.pi.restaurantebarracocina.restaurante;

public class PedidoFinalizado {
	
	private int idComanda;
	private int idMenu;
	
	public PedidoFinalizado(int idComanda, int idMenu){
		this.idComanda = idComanda;
		this.idMenu = idMenu;
	}

	public int getIdComanda() {
		return idComanda;
	}

	public int getIdMenu() {
		return idMenu;
	}

}
