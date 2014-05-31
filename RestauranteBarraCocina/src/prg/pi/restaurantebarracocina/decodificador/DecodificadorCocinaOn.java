package prg.pi.restaurantebarracocina.decodificador;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;
import prg.pi.restaurantebarracocina.restaurante.PedidosEntrantesCB;
import prg.pi.restaurantebarracocina.restaurante.Producto;

public class DecodificadorCocinaOn {
	
	private Document dom;
	private String respuesta;
	
	private ArrayList<PedidosEntrantesCB> pedidosHistorico;
	private ArrayList<PedidosEntrantesCB> pedidosPrincipal;
	
	public DecodificadorCocinaOn(Document dom){
		this.dom = dom;
		pedidosHistorico = new ArrayList<PedidosEntrantesCB>();
		pedidosPrincipal = new ArrayList<PedidosEntrantesCB>();
		
		interpretarRespuesta();
	}
	
	public ArrayList<PedidosEntrantesCB> getPedidosPrincipal(){
		return pedidosPrincipal;
	}
	
	public ArrayList<PedidosEntrantesCB> getPedidosHistorico(){
		return pedidosHistorico;
	}
	
	private void interpretarRespuesta(){
		respuesta = dom.getElementsByTagName("respuesta").item(0).getFirstChild().getNodeValue();
		Log.e("respuesta", respuesta);
		
		if(respuesta.equals("OK+")){
			NodeList nlPedidos = dom.getElementsByTagName("pedido");
			for(int contador = 0; contador < nlPedidos.getLength(); contador++){
				Node pedido = nlPedidos.item(contador);
				NodeList atributos = pedido.getChildNodes();
				int idComanda = Integer.parseInt(atributos.item(0).getFirstChild().getNodeValue());
				int idMenu = Integer.parseInt(atributos.item(1).getFirstChild().getNodeValue());
				String nombreSeccion = atributos.item(2).getFirstChild().getNodeValue();
				String nombreMesa = atributos.item(3).getFirstChild().getNodeValue();
				String nombreProducto = atributos.item(4).getFirstChild().getNodeValue();
				String nombreCantidad = atributos.item(5).getFirstChild().getNodeValue();
				int unidades = Integer.parseInt(atributos.item(6).getFirstChild().getNodeValue());
				int udPedido = Integer.parseInt(atributos.item(7).getFirstChild().getNodeValue());
				int listos = Integer.parseInt(atributos.item(8).getFirstChild().getNodeValue());
				int servidos = Integer.parseInt(atributos.item(9).getFirstChild().getNodeValue());
				
				if(listos == 0){
					pedidosPrincipal.add(new PedidosEntrantesCB(nombreSeccion, nombreMesa, idComanda, unidades, new Producto(idMenu, nombreProducto, nombreCantidad), listos, servidos));
				} else if(unidades == listos){
					pedidosHistorico.add(new PedidosEntrantesCB(nombreSeccion, nombreMesa, idComanda, unidades, new Producto(idMenu, nombreProducto, nombreCantidad), listos, servidos));
				} else {
					pedidosPrincipal.add(new PedidosEntrantesCB(nombreSeccion, nombreMesa, idComanda, unidades, new Producto(idMenu, nombreProducto, nombreCantidad), listos, servidos));
					pedidosHistorico.add(new PedidosEntrantesCB(nombreSeccion, nombreMesa, idComanda, unidades, new Producto(idMenu, nombreProducto, nombreCantidad), listos, servidos));
				}
			}
		}
	}

}
