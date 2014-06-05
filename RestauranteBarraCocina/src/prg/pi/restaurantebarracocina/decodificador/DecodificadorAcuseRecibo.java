package prg.pi.restaurantebarracocina.decodificador;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
/**
 * Clase encargada de decodificar el mensaje AcuseReciboServer del servidor.
 * 
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class DecodificadorAcuseRecibo {

    private Document DOMRespuesta;
    String aceptado = "";
    String explicacion = "";
    /**
	 * Constructor:
	 * 
	 * @param dom [Document] DOM del XMl a interpretar.
	 */
    public DecodificadorAcuseRecibo(Document dom) {
        this.DOMRespuesta = dom;
        interpretarRespuesta();
    }
    /**
	 * Interpreta la respuesta recibida por el servidor.
	 * 
	 */
    private void interpretarRespuesta() {
        NodeList nodeListAceptado = DOMRespuesta.getElementsByTagName("respuesta");
        aceptado = nodeListAceptado.item(0).getChildNodes().item(0).getNodeValue();
        if (aceptado.equals("NO")) {
            NodeList nodeListExplicacion = DOMRespuesta.getElementsByTagName("explicacion");
            explicacion = nodeListExplicacion.item(0).getChildNodes().item(0).getNodeValue();
        }
    }
    /**
	 * Devuelve la respuesta del servidor.
	 * 
	 * @return [String[]] Respuesta del servidor.
	 */
    public String[] getRespuesta() {
        return new String[]{aceptado, explicacion};
    }
    
}
