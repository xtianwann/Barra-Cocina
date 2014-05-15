package prg.pi.restaurantebarracocina.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import prg.pi.restaurantebarracocina.restaurante.PedidoFinalizado;

public class DecodificadorPedidosServidos {
	
	private Document dom;
	private ArrayList<PedidoFinalizado> pedidosFinalizados;
	
	public DecodificadorPedidosServidos(Document dom){
		this.dom = dom;
		pedidosFinalizados = new ArrayList<PedidoFinalizado>();
		extraerServidos();
	}
	
	public PedidoFinalizado[] getFinalizados(){
		return pedidosFinalizados.toArray(new PedidoFinalizado[0]);
	}
	
	private void extraerServidos(){
		NodeList nlServidos = dom.getElementsByTagName("servido");
		for(int contadorServidos = 0; contadorServidos < nlServidos.getLength(); contadorServidos++){
			int idComanda = Integer.parseInt(nlServidos.item(0).getFirstChild().getNodeValue());
			int idMenu = Integer.parseInt(nlServidos.item(1).getFirstChild().getNodeValue());
			
			pedidosFinalizados.add(new PedidoFinalizado(idComanda, idMenu));
		}
	}

}
