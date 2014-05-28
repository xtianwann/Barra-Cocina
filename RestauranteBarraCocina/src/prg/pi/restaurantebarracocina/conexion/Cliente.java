package prg.pi.restaurantebarracocina.conexion;

import java.io.IOException;
import java.net.ConnectException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;



import prg.pi.restaurantebarracocina.decodificador.DecodificadorCocinaOn;
import Conexion.Conexion;
import XML.XML;

/**
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class Cliente {
	private Conexion conn;
	private String mensaje;
	private String respuesta;
	private String ipServidor;
	private DecodificadorCocinaOn decoCocinaOn;

	public Cliente(String mensaje,String ipServidor) {
		this.ipServidor = ipServidor;
		respuesta = "";
		this.mensaje = mensaje;
	}

	public void init() {
		try {
			enviarMensaje(mensaje);
			respuesta = recibirMensaje();
		} catch (NullPointerException e){
			throw new NullPointerException();
		} catch (IOException e) {
			throw new NullPointerException();
		}
		if (respuesta != null && respuesta.length() > 0) {
			Document dom = XML.stringToXml(respuesta);
			NodeList nodeListTipo = dom.getElementsByTagName("tipo");
			String tipo = nodeListTipo.item(0).getChildNodes().item(0).getNodeValue();
			
			if(tipo.equals("CocinaOn")){
				decoCocinaOn = new DecodificadorCocinaOn(dom);
			}
		} else {
			try {
				conn.cerrarConexion();
			} catch (NullPointerException e){
				
			} catch (IOException e) {
			}
			System.out.println("Agotado tiempo de espera...");
		}
	}

	/**
	 * Establece conexión con el servidor y envía el mensaje pasado por
	 * parámetro
	 * 
	 * @param msg mensaje a enviar
	 * @throws IOException
	 * @throws ConnectException
	 */
	public void enviarMensaje(String msg) throws IOException,
			NullPointerException {
		conexion();
		conn.escribirMensaje(msg);
	}

	/**
	 * Espera un mensaje del servidor durante cinco segundos
	 * 
	 * @return String de respuestas del servidor
	 * @return null si excede el límite de tiempo
	 */
	public String recibirMensaje() throws IOException, NullPointerException {
		String respuesta = null;
		long espera = System.currentTimeMillis() + 1000;
		do {
			respuesta = conn.leerMensaje();
		} while (respuesta.length() == 0 || espera < System.currentTimeMillis());
		return respuesta;
	}

	/**
	 * Establece conexión con el servidor
	 * 
	 * @throws IOExceptionb,ConnectException
	 */
	private void conexion() throws IOException, NullPointerException {
		//conn = new Conexion("192.168.1.9", 27000); // juan
		//conn = new Conexion("192.168.20.3", 27000); // cristian
		conn = new Conexion(ipServidor, 27000); // portatil
	}
	
	public DecodificadorCocinaOn getDecoCocinaOn(){
		return decoCocinaOn;
	}
}
