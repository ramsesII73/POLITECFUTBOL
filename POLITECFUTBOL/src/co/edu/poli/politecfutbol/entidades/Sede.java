package co.edu.poli.politecfutbol.entidades;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Esta clase encapsula el concepto de una sede
 * @author apaternina
 *
 */
public class Sede implements Serializable {
	private static final long serialVersionUID = 1L;
	/*
	 * Lista de campos de la clase
	 */
	private String nombre;
	private String direccion;
	private ArrayList<Cancha> canchas; // la lista de canchas ubicadas en esta sede
	private Ciudad ciudad; // la ciudad en la que est� ubicada esta sede
	
	/**
	 * Constructor de la clase
	 * 
	 * @param nombre	El nombre de la sede.
	 * @param direccion La direcci�n de la sede.
	 * @param ciudad	La ciudad en la que est� ubicada la sede.
	 */
	public Sede(String nombre, String direccion, Ciudad ciudad) {
		this.nombre = nombre;
		this.direccion = direccion;
		this.canchas = new ArrayList<Cancha>();
		this.ciudad = ciudad;
	}
	
	public boolean eliminarCancha(String nombreDeCancha) {
		for (int i = 0; i < canchas.size(); i++) {
			if (canchas.get(i).getNombre().equals(nombreDeCancha)) {
				canchas.remove(i);
				return true;
			}
		}
		return false;
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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public ArrayList<Cancha> getCanchas() {
		return canchas;
	}

	public void addCancha(Cancha cancha) {
		this.canchas.add(cancha);
	}

	public Ciudad getCiudad() {
		return ciudad;
	}
}
