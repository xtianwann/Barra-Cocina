package prg.pi.restaurantebarracocina.decodificador;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import prg.pi.restaurantebarracocina.restaurante.PedidoModificadoCamarero;

/**
 * Clase encargada de decodificar el mensaje ModificacionCamarero del servidor.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class DecodificadorCancelarPedido {
	
	private Document dom;
	private PedidoModificadoCamarero pedido;
	
	/**
	 * Constructor:
	 * 
	 * @param dom [Document] DOM del XMl a interpretar.
	 */
	public DecodificadorCancelarPedido(Document dom){
		this.dom = dom;
		extraerCancelados();
	}
	
	/**
	 * Devuelve el pedido cancelado por del servidor.
	 * 
	 * @return [PedidoModificadoCamarero] Pedidos cancelado del servidor.
	 */
	public PedidoModificadoCamarero getCancelado(){
		return pedido;
	}
	
	/**
	 * Interpreta la respuesta recibida por el servidor y genera el pedido cancelado.
	 */
	private void extraerCancelados(){
		/* Obtenemos la información del mensaje */
		Node nodeComanda = dom.getElementsByTagName("pedido").item(0);
		Element elementoComanda = (Element) nodeComanda;
		int idComanda = Integer.parseInt(elementoComanda.getAttribute("idCom"));
		int idMenu = Integer.parseInt(dom.getElementsByTagName("idMenu").item(0).getFirstChild().getNodeValue());
		int unidades = Integer.parseInt(dom.getElementsByTagName("unidades").item(0).getFirstChild().getNodeValue());
		
		pedido = new PedidoModificadoCamarero(idComanda, idMenu, unidades);
	}

}
