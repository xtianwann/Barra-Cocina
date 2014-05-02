package prg.pi.restaurantebarracocina.xml;

import android.util.Log;
import prg.pi.restaurantebarracocina.restaurante.PedidosEntrantesCB;
import XML.XML;

public class XMLPedidosListos extends XML{
	
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
