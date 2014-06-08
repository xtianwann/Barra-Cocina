package prg.pi.restaurantebarracocina.conexion;

import java.io.IOException;
import java.net.ConnectException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import prg.pi.restaurantebarracocina.decodificador.DecodificadorAcuseRecibo;
import prg.pi.restaurantebarracocina.decodificador.DecodificadorCocinaOn;
import Conexion.Conexion;
import XML.XML;

/**
 * Clase encargada de establecer conexión con el servidor, enviarle un mensaje y esperar una respuesta
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class Cliente {
	private Conexion conn;
	private String mensaje;
	private String respuesta;
	private String ipServidor;
	private DecodificadorCocinaOn decoCocinaOn;
	private DecodificadorAcuseRecibo decoAcuse;
	
	/**
	 * Constructor
	 * 
	 * @param mensaje [String] mensaje que se le enviará al servidor
	 * @param ipServidor [String] ip del servidor
	 */
	public Cliente(String mensaje,String ipServidor) {
		this.ipServidor = ipServidor;
		respuesta = "";
		this.mensaje = mensaje;
	}

	/**
	 * Método encargado de enviar el mensaje, obtener la respuesta e interpretarla
	 */
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
			
			/* Tipos de respuesta */
			if(tipo.equals("CocinaOn")){
				decoCocinaOn = new DecodificadorCocinaOn(dom);
			}
			if(tipo.equals("AcuseRecibo")){
				decoAcuse = new DecodificadorAcuseRecibo(dom);
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
	 * @param msg [String] mensaje a enviar
	 * @throws IOException excepción lanzada en caso de que hubiera algún tipo de error en la entrada o salida de datos
	 * @throws ConnectException excepción lanzada en caso de haber algún tipo de error en la conexión
	 */
	public void enviarMensaje(String msg) throws IOException, NullPointerException {
		conexion();
		conn.escribirMensaje(msg);
	}

	/**
	 * Espera un mensaje del servidor durante cinco segundos
	 * 
	 * @return [String] respuesta del servidor, null si excede el límite de tiempo
	 */
	public String recibirMensaje() throws IOException, NullPointerException {
		String respuesta = null;
		int intento = 0;
		do {
			respuesta = conn.leerMensaje();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			intento++;
		} while (respuesta.length() == 0 && intento < 10);
		return respuesta;
	}

	/**
	 * Establece conexión con el servidor
	 * 
	 * @throws IOException, ConnectException
	 */
	private void conexion() throws IOException, NullPointerException {
		conn = new Conexion(ipServidor, 27000);
	}
	
	/**
	 * Permite obtener una instancia del decodificador que obtiene la respuesta al mensaje de encendido de la cocina
	 * 
	 * @return [DecodificadorCocinaOn] instancia del decodificador de la respuesta del mensaje de encendido
	 */
	public DecodificadorCocinaOn getDecoCocinaOn(){
		return decoCocinaOn;
	}
	
	/**
	 * Permite obtener una instancia del decodificador del acuse de recibo
	 * 
	 * @return [DecodificadorAcuseRecibo] instancia del decodificador del acuse de recibo
	 */
	public DecodificadorAcuseRecibo getDecoAcuse(){
		return decoAcuse;
	}
}
