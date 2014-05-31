package prg.pi.restaurantebarracocina.decodificador;

import org.w3c.dom.Document;

public class DecodificadorComandaAcabada {
	
	private Document dom;
	private int idComanda;
	
	public DecodificadorComandaAcabada(Document dom){
		this.dom = dom;
		obtenerDatos();
	}
	
	public int getIdcomandaCerrada(){
		return idComanda;
	}
	
	private void obtenerDatos(){
		idComanda = Integer.parseInt(dom.getElementsByTagName("idComanda").item(0).getFirstChild().getNodeValue());
	}

}
