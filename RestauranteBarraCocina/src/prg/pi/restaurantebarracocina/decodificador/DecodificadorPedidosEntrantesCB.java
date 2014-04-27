package prg.pi.restaurantebarracocina.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
		/* Obtenemos los datos de la mesa */
		NodeList nodeListMesa = dom.getElementsByTagName("mesa");
		Node nodeMesa = nodeListMesa.item(0);
		String nombreMesa = nodeMesa.getFirstChild().getNodeValue();
		NodeList nodeListSeccion = dom.getElementsByTagName("seccion");
		Node nodeSeccion = nodeListSeccion.item(0);
		String nombreSeccion = nodeSeccion.getFirstChild().getNodeValue();
		
		/* Extraemos la información de los pedidos */
		NodeList nodeListPedidos = dom.getElementsByTagName("pedidos");
		Node nodePedidos = nodeListPedidos.item(0);
		Element elementoPedidos = (Element) nodePedidos;
		int idComanda = Integer.parseInt(elementoPedidos.getAttribute("idCom"));
		
		NodeList listaPedidos = dom.getElementsByTagName("pedido");
		for(int contadorPedidos = 0; contadorPedidos < listaPedidos.getLength(); contadorPedidos++){
			Node nodePedido = listaPedidos.item(contadorPedidos);
			Element elementoPedido = (Element) nodePedido;
			int idMenu = Integer.parseInt(elementoPedido.getAttribute("idMenu"));
			NodeList nodeListDatos = nodePedido.getChildNodes();
			String nombreProducto = nodeListDatos.item(0).getFirstChild().getNodeValue();
            String nombreCantidad = nodeListDatos.item(1).getFirstChild().getNodeValue();
            int unidades = Integer.parseInt(nodeListDatos.item(2).getFirstChild().getNodeValue());
            int listos = Integer.parseInt(nodeListDatos.item(3).getFirstChild().getNodeValue());
            
            pedidosEntrantes.add(new PedidosEntrantesCB(nombreSeccion, nombreMesa, idComanda, unidades,new Producto(idMenu, nombreProducto, nombreCantidad), listos));
		}
	}

}
