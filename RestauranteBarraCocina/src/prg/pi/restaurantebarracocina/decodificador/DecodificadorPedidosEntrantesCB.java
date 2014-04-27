package prg.pi.restaurantebarracocina.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

import prg.pi.restaurantebarracocina.restaurante.PedidosEntrantesCB;
import prg.pi.restaurantebarracocina.restaurante.Producto;

public class DecodificadorPedidosEntrantesCB {
	
	private Document dom;
	private ArrayList<PedidosEntrantesCB> pedidosEntrantes;
	
	public DecodificadorPedidosEntrantesCB(Document dom) {
		this.dom = dom;
		pedidosEntrantes = new ArrayList<PedidosEntrantesCB>();
		generarPedidos();
	}
	
	public PedidosEntrantesCB[] getPedidosEntrantes(){
		return pedidosEntrantes.toArray(new PedidosEntrantesCB[0]);
	}
	
	private void generarPedidos(){
		Node nodeMesa = dom.getElementsByTagName("mesa").item(0);
		Element elementoMesa = (Element) nodeMesa;
		int idMes = Integer.parseInt(elementoMesa.getAttribute("idMes"));
		String nombreMesa = nodeMesa.getFirstChild().getNodeValue();
		Node nodeSeccion = dom.getElementsByTagName("seccion").item(0);
		String nombreSeccion = nodeSeccion.getFirstChild().getNodeValue();
		Node nodeIdComanda = dom.getElementsByTagName("idComanda").item(0);
		int idComanda = Integer.parseInt(nodeIdComanda.getFirstChild().getNodeValue());
		
		Node nodePedidos = dom.getElementsByTagName("pedidos").item(0);
		NodeList nodeListPedidos = nodePedidos.getChildNodes();
		for(int pedido = 0; pedido < nodeListPedidos.getLength(); pedido++){
			Node nodePedido = nodeListPedidos.item(pedido);
			Element elementoPedido = (Element) nodePedido;
			int idMenu = Integer.parseInt(elementoPedido.getAttribute("idMenu"));
			String nombreProducto = nodePedido.getChildNodes().item(0).getFirstChild().getNodeValue();
			Log.d("array",nombreProducto);
			String nombreCantidad = nodePedido.getChildNodes().item(1).getFirstChild().getNodeValue();
			int unidades = Integer.parseInt(nodePedido.getChildNodes().item(2).getFirstChild().getNodeValue());
			int listos = Integer.parseInt(nodePedido.getChildNodes().item(3).getFirstChild().getNodeValue());
			
			pedidosEntrantes.add(new PedidosEntrantesCB(nombreSeccion, nombreMesa, idComanda,unidades, new Producto(idMenu, nombreProducto, nombreCantidad), listos));
		}
	}

}
