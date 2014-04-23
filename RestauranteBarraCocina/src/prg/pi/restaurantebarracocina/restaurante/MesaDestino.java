package prg.pi.restaurantebarracocina.restaurante;
import java.util.HashMap;

/**
 * @author Juan Gabriel Pérez Leo
 * @author Crisitan Marín Honor
 */
public class MesaDestino {
    
    private Mesa mesa;
    private HashMap<Pedido, Integer> pedidos; // pedido - cantidad

    public MesaDestino(Mesa mesa, HashMap<Pedido, Integer> pedidos) {
        this.mesa = mesa;
        this.pedidos = pedidos;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public HashMap<Pedido, Integer> getPedidos() {
        return pedidos;
    }
    
    
    
}
