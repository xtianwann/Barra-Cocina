package prg.pi.restaurantebarracocina.decodificador;

import org.w3c.dom.Document;
/**
 * Clase encargada de decodificar el mensaje ComandaAcabada del servidor.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class DecodificadorComandaAcabada {
	
	private Document dom;
	private int idComanda;
	/**
	 * Constructor:
	 * 
	 * @param dom [Document] DOM del XMl a interpretar.
	 */
	public DecodificadorComandaAcabada(Document dom){
		this.dom = dom;
		obtenerDatos();
	}
	/**
	 * Devuelve el id de la comanda recibida por del servidor.
	 * 
	 * @return [int] Id de la comanda.
	 */
	public int getIdcomandaCerrada(){
		return idComanda;
	}
	/**
	 * Interpreta la respuesta recibida por el servidor y genera los el id de la comanda.
	 * 
	 */
	private void obtenerDatos(){
		idComanda = Integer.parseInt(dom.getElementsByTagName("idComanda").item(0).getFirstChild().getNodeValue());
	}

}
