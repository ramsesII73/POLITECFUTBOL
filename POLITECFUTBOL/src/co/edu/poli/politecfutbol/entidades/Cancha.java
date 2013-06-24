package co.edu.poli.politecfutbol.entidades;

import java.io.Serializable;

/**
 * Esta clase encapsula el concepto de una cancha de fútbol
 * 
 * @author apaternina
 *
 */
public class Cancha implements Serializable {
	private static final long serialVersionUID = 1L;
	/*
	 * Lista de campos
	 */
	private String nombre;
	private Sede sede;
	
	/**
	 * Constructor
	 * 
	 * @param nombre El nombre de la cancha.
	 * @param sede La sede en la cual está ubicada la cancha.
	 */
	public Cancha(String nombre, Sede sede) {
		this.nombre = nombre;
		this.sede = sede;
	}
	
	/*
	 * GETTERS & SETTERS
	 */
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Sede getSede() {
		return sede;
	}
}
