package co.edu.poli.politecfutbol.entidades;
import java.util.Date;

/**
 * Esta clase representa una reserva de una cancha de fútbol. Esta clase se debe
 * instanciar cada vez que se haga una reserva.
 * 
 * @author apaternina
 * 
 */
public class Reserva {
	/*
	 * Lista de campos de la clase
	 */
	private Date fechaHoraDeInicio;
	private Date fechaHoraDeFin;
	private FormaDePago formaDePago; // la forma de pago usada
	private Cliente cliente; // el cliente que solicita la reserva
	private String empleado; // el empleado que hace la reserva
	private Cancha cancha; // la cancha que se está reservando
	
	/**
	 * Constructor de la clase
	 * @param fechaHoraDeInicio Fecha y hora de inicio de la reserva.
	 * @param fechaHoraDeFin Fecha y hora de fin de la reserva.
	 * @param formaDePago La forma de pago usada al momento de hacer la reserva.
	 * @param cliente El ciente que solicitó la reserva.
	 * @param empleado El empleado que realizó la reserva.
	 * @param cancha La cancha que se está reservando.
	 */
	public Reserva(Date fechaHoraDeInicio, Date fechaHoraDeFin,
			FormaDePago formaDePago, Cliente cliente, String empleado,
			Cancha cancha) {
		super();
		this.fechaHoraDeInicio = fechaHoraDeInicio;
		this.fechaHoraDeFin = fechaHoraDeFin;
		this.formaDePago = formaDePago;
		this.cliente = cliente;
		this.empleado = empleado;
		this.cancha = cancha;
	}
	
	/*
	 * Getters & Setters
	 */
	public Date getFechaHoraDeInicio() {
		return fechaHoraDeInicio;
	}
	public Date getFechaHoraDeFin() {
		return fechaHoraDeFin;
	}
	public FormaDePago getFormaDePago() {
		return formaDePago;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public String getEmpleado() {
		return empleado;
	}
	public Cancha getCancha() {
		return cancha;
	}
}
