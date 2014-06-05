package prg.pi.restaurantebarracocina.restaurante;
/**
 * 
 * Clase que contiene toda la información sobre las mesas.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Mesa {
	private int id;
	private String nombre;
	/**
     * 
     * Constructor:
     * 
     * @param id [int] Id de la mesa.
     * @param nombre [String] Nombre de la mesa.
     */
	public Mesa(int id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	/**
     * Permite obtener el id de la mesa.
     * 
     * @return [int] Id de la mesa.
     */
	public int getId() {
		return id;
	}
	/**
     * Permite modificar id de la mesa.
     * 
     * @param id [int] Id de la mesa a modificar.
     */
	public void setId(int id) {
		this.id = id;
	}
	/**
     * Permite obtener el nombre de la mesa.
     * 
     * @return [String] Nombre de la mesa.
     */
	public String getNombre() {
		return nombre;
	}
	/**
     * Permite modificar el nombre de la mesa.
     * 
     * @param nombre [String] Nombre de la mesa a modificar.
     */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
