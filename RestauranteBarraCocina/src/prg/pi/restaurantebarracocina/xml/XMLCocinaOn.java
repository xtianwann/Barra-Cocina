package prg.pi.restaurantebarracocina.xml;

import XML.XML;
/**
 * 
 * Clase encargada de formar el xml con toda la informaci�n para informar al servidor que el dispositivo de cocina se ha encendido.
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class XMLCocinaOn extends XML{
	/**
	 * 
	 * Constructor:
	 * 
	 */
	public XMLCocinaOn(){
		init();
		addNodo("tipo", "CocinaOn", "paquete");
	}
}
