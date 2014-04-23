package prg.pi.restaurantebarracocina.decodificador;

import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import prg.pi.restaurantebarracocina.restaurante.Mesa;
import prg.pi.restaurantebarracocina.restaurante.MesaDestino;
import prg.pi.restaurantebarracocina.restaurante.Pedido;
import prg.pi.restaurantebarracocina.restaurante.Producto;

/**
 * @author Juan Gabriel Pérez Leo
 * @author Cristian Marín Honor
 */
public class DecodificadorCocinaBarra {
    
    private Document dom;
    private HashMap<Pedido, Integer> pedidos; // pedido - cantidad
    private MesaDestino mesaDestino;

    public DecodificadorCocinaBarra(Document dom) {
        this.dom = dom;
        pedidos = new HashMap<Pedido,Integer>();
        generarPedidos();
    }
    
    public MesaDestino getMesaDestino(){
        return mesaDestino;
    }
    
    private void generarPedidos(){
        /* Obtenemos los datos de la mesa */
        NodeList nodeListMesa = dom.getElementsByTagName("mesa");
        Node nodeMesa = nodeListMesa.item(0);
        Element elementoMesa = (Element) nodeMesa;
        String idMesa = elementoMesa.getAttribute("idMes");
        int idMes = Integer.parseInt(idMesa);
        String nombreMesa = nodeMesa.getFirstChild().getNodeValue();
        
        /* Extraemos la información de los pedidos */
        NodeList nodeListPedidos = dom.getElementsByTagName("pedidos").item(0).getChildNodes();
        for(int contadorPedidos = 0; contadorPedidos < nodeListPedidos.getLength(); contadorPedidos++){
            Node nodeMenu = nodeListPedidos.item(contadorPedidos);
            Element elementoMenu = (Element) nodeMenu;
            String stringIdMenu = elementoMenu.getAttribute("idMenu");
            int idMenu = Integer.parseInt(stringIdMenu);
            NodeList nodeListDatos = nodeMenu.getChildNodes();
            String nombreProducto = nodeListDatos.item(0).getFirstChild().getNodeValue();
            String nombreCantidad = nodeListDatos.item(1).getFirstChild().getNodeValue();
            int unidades = Integer.parseInt(nodeListDatos.item(2).getFirstChild().getNodeValue());
            
            Pedido pedido = new Pedido(new Producto(idMenu, nombreProducto,nombreCantidad), unidades);
            
            pedidos.put(pedido,0);
        }
        
        mesaDestino = new MesaDestino(new Mesa(idMes,nombreMesa), pedidos);
    }
    
}
