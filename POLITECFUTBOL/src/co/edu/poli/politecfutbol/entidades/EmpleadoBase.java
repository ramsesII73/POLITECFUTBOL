package co.edu.poli.politecfutbol.entidades;
/**
 * Esta clase representa un empleado base, es decir, un empleado con privilegios m�nimos
 * @author apaternina
 *
 */
public class EmpleadoBase {
	/*
	 * Lista de campos de la clase
	 */
	private String idDeEmpleado;
	private String usuario;
	private String password;
	
	/**
	 * Constructor de la clase.
	 * @param idDeEmpleado El n�mero de identificaci�n del empleado.
	 * @param usuario El nombre de usuario para autenticarse en el sistema.
	 * @param password La contrase�a del usuario para autenticarse en el sistema.
	 */
	public EmpleadoBase(String idDeEmpleado, String usuario, String password) {
		super();
		this.idDeEmpleado = idDeEmpleado;
		this.usuario = usuario;
		this.password = password;
	}
	
	/**
	 * Este m�todo se usa para crear una nueva reserva a nombre del cliente especificado.
	 * @param cliente El cliente a nombre de qui�n se har� la reserva.
	 */
	public void realizarReserva(Cliente cliente) {
		//TODO
	}
	
	/**
	 * Este m�todo devuelve un listado de reservas asociadas a un cliente.
	 * @param cliente
	 * @return
	 */
	public Reserva[] consultarReserva(Cliente cliente) {
		//TODO
		return null;
	}

	/*
	 * Getters & Setters
	 */
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdDeEmpleado() {
		return idDeEmpleado;
	}
}
