package co.edu.poli.politecfutbol.entidades;

import java.io.Serializable;

/**
 * Esta clase represent a un cliente de la compañía
 * @author apaternina
 *
 */
public class Cliente implements Serializable {
	/*
	 * Campos de la clase
	 */
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private TipoDeDocumento tipoDeDocumento;
	private String numeroDeDocumento;
	private String telefonoDeContacto;
	private String direccion;
	private String email;
	
	/**
	 * Constructor de la clase
	 * @param primerNombre El primer nombre del cliente.
	 * @param segundoNombre El segundo nombre del cliente.
	 * @param primerApellido El primer apellido del cliente.
	 * @param segundoApellido El segundo apellido del cliente.
	 * @param tipoDeDocumento El tipo de documento de identificación del cliente.
	 * @param numeroDeDocumento El número de documento de identificación del cliente.
	 * @param telefonoDeContacto El  número de teléfono de contacto del cliente.
	 * @param direccion La dirección del cliente.
	 * @param email El correo electrónico del cliente.
	 */
	public Cliente(String primerNombre, String segundoNombre,
			String primerApellido, String segundoApellido,
			TipoDeDocumento tipoDeDocumento, String numeroDeDocumento,
			String telefonoDeContacto, String direccion, String email) {
		this.primerNombre = primerNombre;
		this.segundoNombre = segundoNombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.tipoDeDocumento = tipoDeDocumento;
		this.numeroDeDocumento = numeroDeDocumento;
		this.telefonoDeContacto = telefonoDeContacto;
		this.direccion = direccion;
		this.email = email;
	}
	
	/*
	 * Getters & Setters
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public TipoDeDocumento getTipoDeDocumento() {
		return tipoDeDocumento;
	}

	public void setTipoDeDocumento(TipoDeDocumento tipoDeDocumento) {
		this.tipoDeDocumento = tipoDeDocumento;
	}

	public String getNumeroDeDocumento() {
		return numeroDeDocumento;
	}

	public void setNumeroDeDocumento(String numeroDeDocumento) {
		this.numeroDeDocumento = numeroDeDocumento;
	}

	public String getTelefonoDeContacto() {
		return telefonoDeContacto;
	}

	public void setTelefonoDeContacto(String telefonoDeContacto) {
		this.telefonoDeContacto = telefonoDeContacto;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
