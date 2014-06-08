package prg.pi.restaurantebarracocina.xml;

import prg.pi.restaurantebarracocina.restaurante.PedidosEntrantesCB;
import XML.XML;

/**
 * Clase encargada de formar el xml con toda la información para poner pedidos listos.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLPedidosListos extends XML{
	
	/**
	 * Constructor: genera la estructura del mensaje XML con su contenido
	 * 
	 * @param pedidos [PedidosEntrantesCB[]] Lista de pedidos listos.
	 */
	public XMLPedidosListos(PedidosEntrantesCB[] pedidos){
		init();
		addNodo("tipo", "PedidosListos", "paquete");
		addNodo("salen", null, "paquete");
		for(int contadorPedidos = 0; contadorPedidos < pedidos.length; contadorPedidos++){
			addNodoConAtributos("pedido", new String[]{"idCom"}, new String[]{pedidos[contadorPedidos].getIdComanda()+""}, null, "salen");
			addNodo("idMenu", pedidos[contadorPedidos].getProducto().getIdMenu()+"", "pedido");
			addNodo("listos", pedidos[contadorPedidos].getListos()+"", "pedido");
		}
	}

}
