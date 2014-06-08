package prg.pi.restaurantebarracocina.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import prg.pi.restaurantebarracocina.restaurante.PedidosEntrantesCB;
import prg.pi.restaurantebarracocina.restaurante.Producto;

/**
 * Clase encargada de decodificar el mensaje InfoAcumulada del servidor.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class DecodificadorInfoAcumulada {
	
	private Document dom;
	private ArrayList<PedidosEntrantesCB> pedidosActualizados;
	
	/**
	 * Constructor:
	 * 
	 * @param dom [Document] DOM del XML a interpretar.
	 */
	public DecodificadorInfoAcumulada(Document dom){
		this.dom = dom;
		pedidosActualizados = new ArrayList<PedidosEntrantesCB>();
		extraerInfo();
	}
	
	/**
	 * Devuelve los pedidos pendientes recibidos del servidor.
	 * 
	 * @return [ArrayList<PedidosEntrantesCB>] Pedidos pendientes del servidor.
	 */
	public ArrayList<PedidosEntrantesCB> getInfoActualizada(){
		return pedidosActualizados;
	}
	
	/**
	 * Interpreta la respuesta recibida por el servidor y genera los pedidos pendientes.
	 */
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
			
			/* Genera aquí el objeto */
			pedidosActualizados.add(new PedidosEntrantesCB(nombreSeccion, nombreMesa, idComanda, unidades,new Producto(idMenu, nombreProducto,nombreCantidad), udListo, udServido));
		}
	}

}
