package prg.pi.restaurantebarracocina.xml;

import XML.XML;
/**
 * 
 * Clase encargada de formar el xml con toda la información para hacer logout a una cocina/barra.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class XMLLogout extends XML{
	/**
	 * 
	 * Constructor:
	 * 
	 */
	public XMLLogout(){
		init();
		addNodo("tipo", "LogoutCB", "paquete");
	}

}
