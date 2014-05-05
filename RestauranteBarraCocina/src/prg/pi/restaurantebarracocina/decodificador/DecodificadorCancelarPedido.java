package prg.pi.restaurantebarracocina.decodificador;

import org.w3c.dom.Document;

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
		int idComanda = Integer.parseInt(dom.getElementsByTagName("idCom").item(0).getFirstChild().getNodeValue());
		int idMenu = Integer.parseInt(dom.getElementsByTagName("idMenu").item(0).getFirstChild().getNodeValue());
		int unidades = Integer.parseInt(dom.getElementsByTagName("unidades").item(0).getFirstChild().getNodeValue());
		
		pedido = new PedidoModificadoCamarero(idComanda, idMenu, unidades);
	}

}
