package prg.pi.restaurantebarracocina.conexion;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import Conexion.Conexion;
import XML.XML;

public class Cliente extends Thread {
	private Conexion conn;
	private String mensaje;
	private String respuesta;

	public Cliente(String mensaje) {
		respuesta = "";
		this.mensaje = mensaje;
	}

	public void run() {

		enviarMensaje(mensaje);
		respuesta = recibirMensaje();
		if (respuesta.length() > 0) {
			Document dom = XML.stringToXml(respuesta);
			NodeList nodeListTipo = dom.getElementsByTagName("tipo");
			String tipo = nodeListTipo.item(0).getChildNodes().item(0)
					.getNodeValue();
		} else {
			System.out.println("Agotado tiempo de espera...");
		}
	}

	/**
	 * Establece conexión con el servidor y envía el mensaje pasado por
	 * parámetro
	 * 
	 * @param msg mensaje a enviar
	 */
	public void enviarMensaje(String msg) {
		conexion();
		conn.escribirMensaje(msg);
	}

	/**
	 * Espera un mensaje del servidor durante cinco segundoss
	 * 
	 * @return String de respuestas del servidor
	 * @return null si excede el límite de tiempo
	 */
	public String recibirMensaje() {
		String respuesta = null;
		long espera = System.currentTimeMillis() + 10000;
		do {
			respuesta = conn.leerMensaje();
		} while (respuesta.length() == 0 || espera < System.currentTimeMillis());

		return respuesta;
	}

	/**
	 * Establece conexión con el servidor
	 */
	private void conexion() {
		conn = new Conexion("192.168.43.112", 27014);
	}
}
