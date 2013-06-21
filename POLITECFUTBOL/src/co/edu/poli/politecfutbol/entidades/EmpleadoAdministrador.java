package co.edu.poli.politecfutbol.entidades;

/**
 * Esta clase representa un empleado administrador 
 * @author apaternina
 *
 */
public class EmpleadoAdministrador extends EmpleadoBase {

	/**
	 * Constructor de la clase 
	 * 
	 * @param idDeEmpleado	El id del empleado
	 * @param usuario		El nombre de usuario
	 * @param password
	 */
	public EmpleadoAdministrador(String idDeEmpleado, String usuario,
			String password) {
		super(idDeEmpleado, usuario, password);
	}
}
