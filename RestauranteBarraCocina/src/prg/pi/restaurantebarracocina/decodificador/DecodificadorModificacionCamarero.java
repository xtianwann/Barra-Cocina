package prg.pi.restaurantebarracocina.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import prg.pi.restaurantebarracocina.restaurante.PedidoModificadoCamarero;

public class DecodificadorModificacionCamarero {
	private Document dom;
	private ArrayList<PedidoModificadoCamarero> pedidosListos;
	
	public DecodificadorModificacionCamarero(Document dom){
		this.dom = dom;
		pedidosListos = new ArrayList<PedidoModificadoCamarero>();
		generarPedidos();
	}
	
	public PedidoModificadoCamarero[] getPedidosListos(){
		return pedidosListos.toArray(new PedidoModificadoCamarero[0]);
	}
	
	private void generarPedidos(){
		NodeList nodeListPedidos = dom.getElementsByTagName("pedido");
		for(int pedido = 0; pedido < nodeListPedidos.getLength(); pedido++){
			Node nodePedido = nodeListPedidos.item(pedido);
			Element elementoPedido = (Element) nodePedido;
			int idComanda = Integer.parseInt(elementoPedido.getAttribute("idCom"));
			int idMenu = Integer.parseInt(nodePedido.getChildNodes().item(0).getFirstChild().getNodeValue());
			int listos = Integer.parseInt(nodePedido.getChildNodes().item(1).getFirstChild().getNodeValue());
			int finalizado = Integer.parseInt(nodePedido.getChildNodes().item(2).getFirstChild().getNodeValue());
			
			boolean todosServidos = finalizado == 1 ? true : false;
			pedidosListos.add(new PedidoModificadoCamarero(idComanda, idMenu, listos, todosServidos));
		}
	}
}
