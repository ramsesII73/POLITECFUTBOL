package co.edu.poli.politecfutbol.entidades;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Esta clase encapsula el concepto de una ciudad
 * @author apaternina
 *
 */
public class Ciudad implements Serializable {
	private static final long serialVersionUID = 1L;
	/*
	 * Lista de campos de la clase
	 */
	private String nombre;
	private ArrayList<Sede> sedes; // las sedes ubicadas en esta ciudad
	
	/**
	 * Constructor de la clase
	 * @param nombre El nombre de la ciudad.
	 */
	public Ciudad(String nombre) {
		super();
		this.nombre = nombre;
		this.sedes = new ArrayList<Sede>();
	}
	
	public boolean eliminarSede(String nombreDeSede) {
		for (int i = 0; i < sedes.size(); i++) {
			if (sedes.get(i).getNombre().equals(nombreDeSede)) {
				sedes.remove(i);
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

	public ArrayList<Sede> getSedes() {
		return sedes;
	}

	public void setSedes(Sede sede) {
		this.sedes.add(sede);
	}

	public void addSede(Sede sede) {
		this.sedes.add(sede);
		
	}
}
