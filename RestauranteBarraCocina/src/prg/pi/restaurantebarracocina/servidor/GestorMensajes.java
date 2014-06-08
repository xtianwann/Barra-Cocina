package prg.pi.restaurantebarracocina.servidor;

import Conexion.Conexion;

import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import prg.pi.restaurantebarracocina.MainActivity;
import prg.pi.restaurantebarracocina.decodificador.DecodificadorCancelarPedido;
import prg.pi.restaurantebarracocina.decodificador.DecodificadorComandaAcabada;
import prg.pi.restaurantebarracocina.decodificador.DecodificadorInfoAcumulada;
import prg.pi.restaurantebarracocina.decodificador.DecodificadorModificacionCamarero;
import prg.pi.restaurantebarracocina.decodificador.DecodificadorPedidosEntrantesCB;
import prg.pi.restaurantebarracocina.decodificador.DecodificadorPedidosServidos;

/**
 * Interpreta la cabecera del mensaje y ejecuta la acción que le corresponde
 * para tratarlo.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class GestorMensajes extends Thread {

	private Socket socket;
	private Conexion conn;
	private String mensaje;
	private MainActivity principal;

	/**
     * Constructor
     * 
     * @param socket [Scoket] socket por el que un dispositivo está estableciendo
     * conexión con el servidor
     */
	public GestorMensajes(Socket socket, MainActivity principal) {
		this.principal = principal;
		this.socket = socket;
		try {
			conn = new Conexion(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		do {
			try {
				this.mensaje = conn.leerMensaje();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (this.mensaje.length() == 0);
	}

	public void run() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			mensaje = mensaje.substring(mensaje.indexOf("<"));
			builder = factory.newDocumentBuilder();
			final Document dom = builder.parse(new InputSource(new StringReader(
					mensaje)));

			NodeList nodo = dom.getElementsByTagName("tipo");
			String tipo = nodo.item(0).getChildNodes().item(0).getNodeValue();
			
			/* Tipos de mensajes que puede recibir */
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
			
			if (tipo.equals("InfoAcumulada")) {
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
			
			if (tipo.equals("TodosServidos")) {
				new Thread(new Runnable() {
					public void run() {
						principal.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								DecodificadorPedidosServidos pedidos = new DecodificadorPedidosServidos(dom);
								principal.todosServidos(pedidos.getFinalizados());
							}
						});
					}
				}).start();
			}
			
			if (tipo.equals("ComandaAcabada")) {
				new Thread(new Runnable() {
					public void run() {
						principal.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								DecodificadorComandaAcabada finComanda = new DecodificadorComandaAcabada(dom);
								principal.finalizarComanda(finComanda.getIdcomandaCerrada());
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
