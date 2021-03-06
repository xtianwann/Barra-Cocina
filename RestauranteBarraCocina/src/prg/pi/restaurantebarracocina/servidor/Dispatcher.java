package prg.pi.restaurantebarracocina.servidor;

import Cola.ColaSincronizadaSocket;
import Excepciones.Cola.ExcepcionColaLlena;
import Excepciones.Cola.ExcepcionColaVacia;
import Excepciones.ExcepcionInesperada;
import java.net.Socket;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import prg.pi.restaurantebarracocina.MainActivity;

/**
 * Coge un socket de la lista y se lo da al gestor de mensajes
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class Dispatcher extends Thread{
    
    private ColaSincronizadaSocket cola;
    private boolean parado;
    private Servidor.HiloPrincipal hiloPrincipal;
    private MainActivity principal;
    
    /**
     * Constructor
     */
    public Dispatcher(){
        cola = new ColaSincronizadaSocket();
        hiloPrincipal = null;
    }
    
    /**
     * Constructor
     * 
     * @param hiloPrincipal [Servidor.HiloPrincipal] instancia del hilo principal
     * @param principal [MainActivity] instancia de la actividad principal
     */
    public Dispatcher(Servidor.HiloPrincipal hiloPrincipal, MainActivity principal){
    	this.principal = principal;
        this.hiloPrincipal = hiloPrincipal;
        cola = new ColaSincronizadaSocket();
    }
    
    /**
     * A�ade un socket a la cola de conexiones.
     * 
     * @param socket [Socket] socket a trav�s del cual un disositivo se conecta con el servidor.
     */
    public void addSocket(Socket socket){
        try {
            cola.addSocket(socket);
        } catch (ExcepcionColaLlena ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExcepcionInesperada ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        while(!parado){
            if(!cola.isListaVacia()){
                try {
                    Socket socket = cola.getSocket();
                    new GestorMensajes(socket,principal).start();
                } catch (TimeoutException ex) {
                    Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExcepcionColaVacia ex) {
                    Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExcepcionInesperada ex) {
                    Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Permite saber si el Dispatcer est� parado o no.
     * 
     * @return [boolean] true si est� parado, false en caso contrario.
     */
    public boolean isParado() {
        return parado;
    }

    /**
     * Permite modificar el estado del Dispatcher a parado o corriendo.
     * 
     * @param parado [boolean] true para pararlo, false para que funcione.
     */
    public void setParado(boolean parado) {
        this.parado = parado;
    }
    
}
