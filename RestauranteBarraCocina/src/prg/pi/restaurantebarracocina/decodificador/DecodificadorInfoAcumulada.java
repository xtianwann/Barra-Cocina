package prg.pi.restaurantebarracocina.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import prg.pi.restaurantebarracocina.restaurante.PedidoActualizado;

public class DecodificadorInfoAcumulada {
	
	private Document dom;
	private ArrayList<PedidoActualizado> pedidosActualizados;
	
	public DecodificadorInfoAcumulada(Document dom){
		this.dom = dom;
		pedidosActualizados = new ArrayList<PedidoActualizado>();
		extraerInfo();
	}
	
	public ArrayList<PedidoActualizado> getInfoActualizada(){
		return pedidosActualizados;
	}
	
	private void extraerInfo(){
		NodeList listaPedidos = dom.getElementsByTagName("pedido");
		for(int pedido = 0; pedido < listaPedidos.getLength(); pedido++){
			NodeList atributos = listaPedidos.item(pedido).getChildNodes();
			int idMenu = Integer.parseInt(atributos.item(0).getFirstChild().getNodeValue());
			int idComanda = Integer.parseInt(atributos.item(1).getFirstChild().getNodeValue());
			String nombreSeccion = atributos.item(2).getFirstChild().getNodeValue();
			String nombreMesa = atributos.item(3).getFirstChild().getNodeValue();
			String nombreProducto = atributos.item(4).getFirstChild().getNodeValue();
			String nombreCantidad = atributos.item(5).getFirstChild().getNodeValue();
			int unidades = Integer.parseInt(atributos.item(6).getFirstChild().getNodeValue());
			int udPedido = Integer.parseInt(atributos.item(7).getFirstChild().getNodeValue());
			int udListo = Integer.parseInt(atributos.item(8).getFirstChild().getNodeValue());
			int udServido = Integer.parseInt(atributos.item(9).getFirstChild().getNodeValue());
			
			// genera aquí el objeto
			pedidosActualizados.add(new PedidoActualizado(nombreSeccion,nombreMesa,idMenu,idComanda, nombreProducto,nombreCantidad, unidades, udPedido, udListo, udServido));
		}
	}

}
