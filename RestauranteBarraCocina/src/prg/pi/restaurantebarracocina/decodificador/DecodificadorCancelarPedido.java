package prg.pi.restaurantebarracocina.decodificador;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import prg.pi.restaurantebarracocina.restaurante.PedidoModificadoCamarero;

public class DecodificadorCancelarPedido {
	
	private Document dom;
	private PedidoModificadoCamarero pedido;
	
	public DecodificadorCancelarPedido(Document dom){
		this.dom = dom;
		extraerCancelados();
	}
	
	public PedidoModificadoCamarero getCancelado(){
		return pedido;
	}
	
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
