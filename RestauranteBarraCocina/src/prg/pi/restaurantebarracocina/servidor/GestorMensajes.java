package prg.pi.restaurantebarracocina.servidor;

import Conexion.Conexion;

import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import prg.pi.restaurantebarracocina.MainActivity;
import prg.pi.restaurantebarracocina.decodificador.DecodificadorCancelarPedido;
import prg.pi.restaurantebarracocina.decodificador.DecodificadorCocinaBarra;
import prg.pi.restaurantebarracocina.decodificador.DecodificadorInfoAcumulada;
import prg.pi.restaurantebarracocina.decodificador.DecodificadorModificacionCamarero;
import prg.pi.restaurantebarracocina.decodificador.DecodificadorPedidosEntrantesCB;
import prg.pi.restaurantebarracocina.restaurante.Mesa;
import prg.pi.restaurantebarracocina.restaurante.MesaDestino;
import prg.pi.restaurantebarracocina.restaurante.Pedido;
import prg.pi.restaurantebarracocina.restaurante.PedidosEntrantesCB;
import prg.pi.restaurantebarracocina.restaurante.Producto;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class GestorMensajes extends Thread {

	private Socket socket;
	Conexion conn;
	private String mensaje;
	private MainActivity principal;

	public GestorMensajes(Socket socket, MainActivity principal) {
		this.principal = principal;
		this.socket = socket;
		try {
			conn = new Conexion(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		do {
			try {
				this.mensaje = conn.leerMensaje();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (this.mensaje.length() == 0);
	}

	public void run() {
		try {
			System.out.println("GestorMesaje: Mensaje!");
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			mensaje = mensaje.substring(mensaje.indexOf("<"));
			builder = factory.newDocumentBuilder();
			final Document dom = builder.parse(new InputSource(new StringReader(
					mensaje)));

			NodeList nodo = dom.getElementsByTagName("tipo");
			String tipo = nodo.item(0).getChildNodes().item(0).getNodeValue();

			if (tipo.equals("PedidoMesa")) {
				new Thread(new Runnable() {

					public void run() {
						principal.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								DecodificadorPedidosEntrantesCB pedidos = new DecodificadorPedidosEntrantesCB(dom);
								principal.addPedidos(pedidos.getPedidosEntrantes());
							}
						});
					}
				}).start();
			}
			if (tipo.equals("ModificacionCamarero")) {
				new Thread(new Runnable() {

					public void run() {
						principal.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								DecodificadorModificacionCamarero pedidos = new DecodificadorModificacionCamarero(dom);
								principal.modificarUnidades(pedidos.getPedidosListos()[0]);
							}
						});
					}
				}).start();
			}
			if (tipo.equals("CancelarPedido")) {
				new Thread(new Runnable() {

					public void run() {
						principal.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								DecodificadorCancelarPedido pedidos = new DecodificadorCancelarPedido(dom);
								principal.cancelarPedidos(pedidos.getCancelado());
							}
						});
					}
				}).start();
			}
			if (tipo.equals("XMLInfoAcumulada")) {
				new Thread(new Runnable() {

					public void run() {
						principal.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								DecodificadorInfoAcumulada pedidos = new DecodificadorInfoAcumulada(dom);
								principal.actualizarPedidos(pedidos.getInfoActualizada());
							}
						});
					}
				}).start();
			}

		} catch (SAXException ex) {
			Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
}
