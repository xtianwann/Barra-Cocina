package prg.pi.restaurantebarracocina.restaurante;

public class PedidoModificadoCamarero {
	
	private int idComanda;
	private int idMenu;
	private int unidades;
	private boolean todosServidos; 
	
	public PedidoModificadoCamarero(int idComanda, int idMenu, int unidades){
		this.idComanda = idComanda;
		this.idMenu = idMenu;
		this.unidades = unidades;
		this.todosServidos = false;
	}
	
	public PedidoModificadoCamarero(int idComanda, int idMenu, int unidades, boolean todosServidos){
		this.idComanda = idComanda;
		this.idMenu = idMenu;
		this.unidades = unidades;
		this.todosServidos = todosServidos;
	}

	public int getIdComanda() {
		return idComanda;
	}

	public void setIdComanda(int idComanda) {
		this.idComanda = idComanda;
	}

	public int getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
	}

	public int getUnidades() {
		return unidades;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}

	public boolean isTodosServidos() {
		return todosServidos;
	}

	public void setTodosServidos(boolean todosServidos) {
		this.todosServidos = todosServidos;
	}
}
