package prg.pi.restaurantebarracocina.decodificador;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class DecodificadorAcuseRecibo {

    private Document DOMRespuesta;
    String aceptado = "";
    String explicacion = "";

    public DecodificadorAcuseRecibo(Document dom) {
        this.DOMRespuesta = dom;
        interpretarRespuesta();
    }

    private void interpretarRespuesta() {
        NodeList nodeListAceptado = DOMRespuesta.getElementsByTagName("respuesta");
        aceptado = nodeListAceptado.item(0).getChildNodes().item(0).getNodeValue();
        if (aceptado.equals("NO")) {
            NodeList nodeListExplicacion = DOMRespuesta.getElementsByTagName("explicacion");
            explicacion = nodeListExplicacion.item(0).getChildNodes().item(0).getNodeValue();
        }
    }

    public String[] getRespuesta() {
        return new String[]{aceptado, explicacion};
    }
    
}
