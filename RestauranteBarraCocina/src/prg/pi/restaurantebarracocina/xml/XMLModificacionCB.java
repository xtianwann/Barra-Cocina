package prg.pi.restaurantebarracocina.xml;

import prg.pi.restaurantebarracocina.restaurante.PedidosEntrantesCB;
import XML.XML;
/**
 * 
 * Clase encargada de formar el xml con toda la información modificar un pedido de la cocina.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLModificacionCB extends XML{
	/**
	 * 
	 * Constructor:
	 * 
	 * @param pedidos [PedidosEntrantesCB[]] Lista de pedidos a modificar.
	 */
	public XMLModificacionCB(PedidosEntrantesCB[] pedidos){
		init();
		addNodo("tipo", "ModificacionCB", "paquete");
		addNodo("salen", null, "paquete");
		for(int contadorPedidos = 0; contadorPedidos < pedidos.length; contadorPedidos++){
			addNodoConAtributos("pedido", new String[]{"idCom"}, new String[]{pedidos[contadorPedidos].getIdComanda()+""}, null, "salen");
			addNodo("idMenu", pedidos[contadorPedidos].getProducto().getIdMenu()+"", "pedido");
			addNodo("listos", pedidos[contadorPedidos].getListos()+"", "pedido");
		}
	}

}
