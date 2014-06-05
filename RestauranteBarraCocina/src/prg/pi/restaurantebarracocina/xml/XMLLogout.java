package prg.pi.restaurantebarracocina.xml;

import XML.XML;
/**
 * 
 * Clase encargada de formar el xml con toda la informaci�n para hacer logout a una cocina/barra.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
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
