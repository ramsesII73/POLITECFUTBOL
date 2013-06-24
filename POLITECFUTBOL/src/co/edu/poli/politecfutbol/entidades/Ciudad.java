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
}
