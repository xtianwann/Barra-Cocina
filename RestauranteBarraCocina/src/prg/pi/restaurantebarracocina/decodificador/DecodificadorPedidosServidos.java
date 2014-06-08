package prg.pi.restaurantebarracocina.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import prg.pi.restaurantebarracocina.restaurante.PedidoFinalizado;

/**
 * Clase encargada de decodificar el mensaje PedidosServidos del servidor.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class DecodificadorPedidosServidos {
	
	private Document dom;
	private ArrayList<PedidoFinalizado> pedidosFinalizados;
	
	/**
	 * Constructor:
	 * 
	 * @param dom [Document] DOM del XMl a interpretar.
	 */
	public DecodificadorPedidosServidos(Document dom){
		this.dom = dom;
		pedidosFinalizados = new ArrayList<PedidoFinalizado>();
		extraerServidos();
	}
	
	/**
	 * Devuelve el pedidos finalizados recibido del servidor.
	 * 
	 * @return [PedidoFinalizado[ ]] Pedidos finalizados del servidor.
	 */
	public PedidoFinalizado[] getFinalizados(){
		return pedidosFinalizados.toArray(new PedidoFinalizado[0]);
	}
	
	/**
	 * Interpreta la respuesta recibida por el servidor y genera los pedidos finalizados.
	 */
	private void extraerServidos(){
		NodeList nlServidos = dom.getElementsByTagName("pedido");
		for(int contadorServidos = 0; contadorServidos < nlServidos.getLength(); contadorServidos++){
			Node servido = nlServidos.item(contadorServidos);
			int idComanda = Integer.parseInt(servido.getChildNodes().item(0).getFirstChild().getNodeValue());
			int idMenu = Integer.parseInt(servido.getChildNodes().item(1).getFirstChild().getNodeValue());
			
			pedidosFinalizados.add(new PedidoFinalizado(idComanda, idMenu));
		}
	}

}
