package prg.pi.restaurantebarracocina.xml;

import XML.XML;
/**
 * 
 * Clase encargada de formar el xml con toda la información para informar al servidor que el dispositivo de cocina se ha encendido.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
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
