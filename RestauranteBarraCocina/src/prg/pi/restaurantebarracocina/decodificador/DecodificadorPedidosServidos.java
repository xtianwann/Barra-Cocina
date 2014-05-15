package prg.pi.restaurantebarracocina.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

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
		NodeList nlServidos = dom.getElementsByTagName("pedido");
		for(int contadorServidos = 0; contadorServidos < nlServidos.getLength(); contadorServidos++){
			Node servido = nlServidos.item(contadorServidos);
			int idComanda = Integer.parseInt(servido.getChildNodes().item(0).getFirstChild().getNodeValue());
			int idMenu = Integer.parseInt(servido.getChildNodes().item(1).getFirstChild().getNodeValue());
			Log.e("decodificador", idComanda+" "+idMenu);
			pedidosFinalizados.add(new PedidoFinalizado(idComanda, idMenu));
		}
	}

}
