package co.edu.poli.politecfutbol.entidades;
/**
 * Esta clase encapsula el concepto de una ciudad
 * @author apaternina
 *
 */
public class Ciudad {

	/*
	 * Lista de campos de la clase
	 */
	private String nombre;
	private Sede[] sedes; // las sedes ubicadas en esta ciudad
	
	/**
	 * Constructor de la clase
	 * @param nombre El nombre de la ciudad.
	 * @param sedes	La lista de sedes ubicadas en esta ciudad.
	 */
	public Ciudad(String nombre, Sede[] sedes) {
		super();
		this.nombre = nombre;
		this.sedes = sedes;
	}

	/*
	 * Getters & Setters
	 */
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Sede[] getSedes() {
		return sedes;
	}

	public void setSedes(Sede[] sedes) {
		this.sedes = sedes;
	}
}
