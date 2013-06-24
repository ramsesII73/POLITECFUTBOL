package co.edu.poli.politecfutbol.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.poli.politecfutbol.entidades.Cancha;
import co.edu.poli.politecfutbol.entidades.Ciudad;
import co.edu.poli.politecfutbol.entidades.Cliente;
import co.edu.poli.politecfutbol.entidades.FormaDePago;
import co.edu.poli.politecfutbol.entidades.Reserva;
import co.edu.poli.politecfutbol.entidades.Sede;
import co.edu.poli.politecfutbol.entidades.TipoDeDocumento;

/**
 * Esta clase se encarga de mostrar la interfaz interactiva por línea de
 * comandos
 * 
 * @author apaternina
 * 
 */
public class UI {

	private static final transient Logger log = LoggerFactory
			.getLogger(UI.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new UI();

	}

	private Scanner sc;
	private Properties properties;
	private ArrayList<Cancha> canchas;
	private ArrayList<Ciudad> ciudades;
	private ArrayList<Cliente> clientes;
	private ArrayList<Reserva> reservas;
	private ArrayList<Sede> sedes;

	private Subject currentUser;

	public UI() {
		sc = new Scanner(System.in);

		mostrarLogo();

		cargarParametrosDelSistema();

		cargarDatos();

		montarShiro();

		login();

		mostrarMenu();
	}

	/**
	 * Este método se usa para cargar los datos de los archivos serializados,
	 * los cuales deben estar presentes dentro del proyecto en las rutas
	 * previamente parametrizadas en el archivo de propiedades.
	 */
	private void cargarDatos() {
		// Chequear si hay archivos serializados
		try (ObjectInputStream oisCanchas = new ObjectInputStream(
				new FileInputStream(properties.getProperty("rutaCanchas")));
				ObjectInputStream oisCiudades = new ObjectInputStream(
						new FileInputStream(
								properties.getProperty("rutaCiudades")));
				ObjectInputStream oisClientes = new ObjectInputStream(
						new FileInputStream(
								properties.getProperty("rutaClientes")));
				ObjectInputStream oisReservas = new ObjectInputStream(
						new FileInputStream(
								properties.getProperty("rutaReservas")));
				ObjectInputStream oisSedes = new ObjectInputStream(
						new FileInputStream(properties.getProperty("rutaSedes")));) {
			// si los hay cargar la información al sistema
			canchas = (ArrayList<Cancha>) oisCanchas.readObject();
			ciudades = (ArrayList<Ciudad>) oisCiudades.readObject();
			clientes = (ArrayList<Cliente>) oisClientes.readObject();
			reservas = (ArrayList<Reserva>) oisReservas.readObject();
			sedes = (ArrayList<Sede>) oisSedes.readObject();
		} catch (FileNotFoundException fne) {
			/*
			 * en caso de que no exista algún archivo de base de datos o falte
			 * alguno de éstos
			 */
			System.out
					.println("No se encontró una base de datos existente, se procederá a crear una base de datos inicial");
			crearBaseDeDatosInicial();
		} catch (IOException ioe) {
			/*
			 * En caso de que ocurra un error de IO diferente a que los archivos
			 * no existan.
			 */
			ioe.printStackTrace();
			System.out
					.println("Ocurrió un error desconocido al leer la base de datos.");
			System.out
					.println("¿Desea crear una nueva base de datos? (nota: se perderán todos los datos salvados previamente)");
			System.out.print("Elija una opción [s/n]: ");

			String opcion = sc.next();
			sc.nextLine();

			switch (opcion) {
			case "s":
				System.out
						.println("Se procederá a crear la base de datos inicial");
				crearBaseDeDatosInicial();
				break;
			case "n":
				System.out
						.println("Se cerrará la aplicación. Por favor contacte al administrador");
				System.exit(0);
				break;
			default:
				System.out
						.println("Se ha seleccionado una opción inválida. No se harán cambios a la aplicación. Adios");
				System.exit(0);
			}
		} catch (ClassNotFoundException cnfe) {
			/*
			 * En caso de que los archivos serializados no correspondan a las
			 * clases esperadas, lo cual puede ocurrir por manipulación externa
			 * de dichos archivos
			 */
			System.out
					.println("Archivos de base de datos corruptos. La aplicación se cerrará.");
			System.exit(0);
		}
	}

	private void cargarParametrosDelSistema() {
		// cargar archivo de propiedades
		properties = new Properties();
		try (FileInputStream fis = new FileInputStream(
				"src/properties/systemparameters.properties")) {
			properties.load(fis);
		} catch (FileNotFoundException fnfe) {
			System.out
					.println("No se econtró el archivo de parámetros del sistema");
			System.exit(0);
		} catch (IOException ioe) {
			System.out
					.println("Error al cargar el archivo de parámetros del sistema");
			System.exit(0);
		}
	}

	private void consultarReserva() {
		System.out
				.println("Este es el listado de reservas registradas en el sistema:");
		for (Reserva r : reservas) {
			System.out.println("Primer Apellido: "
					+ r.getCliente().getPrimerApellido()
					+ "\n"
					+ "Primer Nombre: "
					+ r.getCliente().getPrimerNombre()
					+ "\n"
					+ "Inicio: "
					+ DateFormat.getDateTimeInstance().format(
							r.getFechaHoraDeInicio().getTime())
					+ "\n"
					+ "Fin: "
					+ DateFormat.getDateTimeInstance().format(
							r.getFechaHoraDeFin().getTime()));
			for (Cancha c : r.getCanchas()) {
				System.out.println("\tCancha: " + c.getNombre());
				System.out.println("\tSede: " + c.getSede().getNombre());
				System.out.println("\tCiudad: "
						+ c.getSede().getCiudad().getNombre());
			}
		}
	}

	/**
	 * Este método crea la base de datos inicial de la aplicación
	 */
	private void crearBaseDeDatosInicial() {
		/*
		 * Se instancian los objetos que harán parte de la base de datos
		 */
		canchas = new ArrayList<Cancha>();
		ciudades = new ArrayList<Ciudad>();
		clientes = new ArrayList<Cliente>();
		reservas = new ArrayList<Reserva>();
		sedes = new ArrayList<Sede>();

		/*
		 * Se llama al método de serialización
		 */
		serializar();
	}

	private void crearCancha() {
		String nombreDeCancha;
		Sede sede;

		System.out.println("Bienvenido al sistema de creación de Canchas");

		if (sedes.size() < 1) {
			System.out
					.println("Aún no se han ingresado sedes al sistema. Adios");
			System.exit(0);
		} else {
			// se pide el nombre de la cancha
			System.out.print("Ingrese el nombre de la cancha: ");

			nombreDeCancha = sc.nextLine();

			// se pide la sede en la que está ubicada la cancha
			System.out
					.println("Ingrese la sede en la que está ubicada la cancha:");
			int contador = 1;
			for (Sede s : sedes) {
				System.out.println(contador + ". " + s.getNombre());
			}
			System.out.print("Elija una opción [1-" + sedes.size() + "]: ");

			int opcion = sc.nextInt();
			sc.nextLine();

			sede = sedes.get(opcion - 1);

			// se crea la cancha
			Cancha cancha = new Cancha(nombreDeCancha, sede);
			canchas.add(cancha);

			// se guarda la referencia de la cancha en la sede
			sede.addCancha(cancha);
			serializar();
			System.out.println("Se creó la cancha exitosamente");
		}
	}

	private void crearCiudad() {
		String nombre;

		// se pide el nombre de la ciudad
		System.out.print("Ingrese el nombre de la ciudad: ");

		nombre = sc.next();

		// se crea la ciudad
		ciudades.add(new Ciudad(nombre));
		serializar();
		System.out.println("La ciudad se ha creado exitosamente");
	}

	private void crearReserva() {
		/*
		 * Variables locales necesarias para realizar la reserva
		 */
		Cliente cliente;
		FormaDePago formaDePago = null;
		String primerNombre;
		String segundoNombre;
		String primerApellido;
		String segundoApellido;
		TipoDeDocumento docType;
		String numeroDeDocumento;
		String numeroDeTelefono;
		String direccion;
		String email;

		if (canchas.size() == 0) {
			System.out
					.println("Aún no se han creado canchas en el sistema. Adios");
			System.exit(0);
		} else {

			/*
			 * Sección de datos del Cliente
			 */
			System.out.println("Información del Cliente");

			// se pide el primer nombre
			System.out.print("Ingrese el primer nombre del cliente: ");

			primerNombre = sc.next();
			sc.nextLine();

			// se pide el segundo nombre
			System.out.print("Ingrese el segundo nombre del cliente: ");

			segundoNombre = sc.next();
			sc.nextLine();

			System.out.print("Ingrese el primer apellido del cliente: ");

			// se pide el primer apellido
			primerApellido = sc.next();
			sc.nextLine();

			System.out.print("Ingrese el segundo apellido del cliente: ");

			// se pide el segundo apellido
			segundoApellido = sc.next();
			sc.nextLine();

			// se pide el tipo de documento de identificación
			System.out
					.println("Ingrese el tipo de documento de identificación: ");
			System.out.println("1. Cédula");
			System.out.println("2. NIT");
			System.out.println("3. Pasaporte");
			System.out.print("Elija una opción [1-3]: ");

			int tipoDeDocumento = sc.nextInt();
			docType = null;

			switch (tipoDeDocumento) {
			case 1:
				docType = TipoDeDocumento.CC;
				break;
			case 2:
				docType = TipoDeDocumento.NIT;
				break;
			case 3:
				docType = TipoDeDocumento.Pasaporte;
				break;
			default:
				System.out.println("Ha ingresado una opción inválida.");
				return;
			}

			// se pide el número de documento
			System.out.print("Ingrese el número de documento: ");

			numeroDeDocumento = sc.next();
			sc.nextLine();

			// se pide el número de teléfono
			System.out.print("Ingrese el teléfono de contacto: ");

			numeroDeTelefono = sc.next();
			sc.nextLine();

			// se pide la dirección del cliente
			System.out.print("Ingrese la dirección del cliente: ");

			direccion = sc.next();
			sc.nextLine();

			// se pide el email del cliente
			System.out.print("Ingrese el e-mail del cliente: ");

			email = sc.next();
			sc.nextLine();

			// se crea el objeto cliente con toda la información recopilada
			cliente = new Cliente(primerNombre, segundoNombre, primerApellido,
					segundoApellido, docType, numeroDeDocumento,
					numeroDeTelefono, direccion, email);

			clientes.add(cliente);

			/*
			 * Sección de datos de la reserva
			 */
			System.out.println("INFORMACIÓN DE LA RESERVA");

			// se pide la información de la cancha o canchas a reservar

			System.out
					.println("Elija las canchas que quiere reservar (EJ: 1,3,5):");
			int contador = 1;
			for (Cancha c : canchas) {
				System.out.println(contador + ". " + c.getNombre());
			}
			System.out
					.print("Escriba las opciones que desea elejir separadas por coma ',' [1-"
							+ canchas.size() + "]: ");

			StringTokenizer st = new StringTokenizer(sc.nextLine(), ",");

			// hacer una lista de canchas escogidas

			ArrayList<Cancha> canchasEscogidas = new ArrayList<Cancha>();

			while (st.hasMoreTokens()) {
				canchasEscogidas.add(canchas.get(Integer.parseInt(st
						.nextToken())));
			}

			// se pide la fecha de la reserva
			System.out.print("Ingrese el día de la reserva (DD/MM/AAAA): ");

			String fecha = sc.next();
			sc.nextLine();

			st = new StringTokenizer(fecha, "/");

			int dia = Integer.parseInt(st.nextToken());
			int mes = Integer.parseInt(st.nextToken());
			int year = Integer.parseInt(st.nextToken());

			// se pide la hora inicial de la reserva
			System.out.print("Ingrese la hora inicial ('HH:MM', ej. 14:00): ");

			String horaInicial = sc.next();
			sc.nextLine();

			StringTokenizer stHora = new StringTokenizer(horaInicial, ":");

			int horas = Integer.parseInt(stHora.nextToken());
			int minutos = Integer.parseInt(stHora.nextToken());

			// se crea el fecha hora inicial
			GregorianCalendar fechaHoraInicial = new GregorianCalendar(year,
					mes, dia, horas, minutos);

			// se pide la hora final de la reserva
			System.out.print("Ingrese la hora final ('HH/MM', ej. 14:00): ");

			String horaFinal = sc.next();
			sc.nextLine();

			StringTokenizer stHoraFinal = new StringTokenizer(horaFinal, ":");

			int horasFinal = Integer.parseInt(stHoraFinal.nextToken());
			int minutosFinal = Integer.parseInt(stHoraFinal.nextToken());

			// se crea el fecha hora final

			GregorianCalendar fechaHoraFinal = new GregorianCalendar(year, mes,
					dia, horasFinal, minutosFinal);

			// se valida que el tiempo de reserva sea mayor o igual a una hora
			if (fechaHoraFinal.getTimeInMillis()
					- fechaHoraInicial.getTimeInMillis() < (60 * 60 * 1000)) {
				System.out
						.println("Error. El tiempo mínimo de reserva es de una hora.");
				return;
			}

			// se calcula el total a pagar
			boolean pedidoValido = calcularTotal(canchasEscogidas.size(),
					fechaHoraInicial, fechaHoraFinal);

			if (!pedidoValido) {
				System.out
						.println("El pedido presenta conflictos de horario. Por favor intente nuevamente.");
				System.exit(0);
			}

			// se pide la forma de pago usada
			System.out.println("Ingrese la forma de pago:");
			System.out.println("1. Tarjeta de Crédito");
			System.out.println("2. Débito");
			System.out.println("3. Cheque");
			System.out.println("4. Bonos");
			System.out.print("Elija una opción [1-4]: ");

			int opcionDePago = sc.nextInt();

			switch (opcionDePago) {
			case 1:
				formaDePago = FormaDePago.TarjetaDeCredito;
				break;
			case 2:
				formaDePago = FormaDePago.Debito;
				break;
			case 3:
				formaDePago = FormaDePago.Cheque;
				break;
			case 4:
				formaDePago = FormaDePago.Bonos;
			default:
				System.out.println("Forma de Pago inválida");
			}

			// se crea el objeto de reserva
			reservas.add(new Reserva(fechaHoraInicial, fechaHoraFinal,
					formaDePago, cliente,
					currentUser.getPrincipal().toString(), canchasEscogidas));
			serializar();
			System.out.println("La reserva se creó exitosamente");
		}
	}

	private boolean calcularTotal(int size, GregorianCalendar fechaHoraInicial,
			GregorianCalendar fechaHoraFinal) {

		/*
		 * Primero se valida que el pedido no supere una extensión de más de un
		 * día puesto que no hay horarios definidos para medianoche
		 */
		int diaDeLaSemana;
		if (fechaHoraInicial.get(Calendar.DAY_OF_WEEK) != fechaHoraFinal
				.get(Calendar.DAY_OF_WEEK)) {
			return false;
		} else {
			diaDeLaSemana = fechaHoraInicial.get(Calendar.DAY_OF_WEEK);
		}

		/*
		 * Luego se valida si se trata de un día feriado
		 */
		// TODO

		/*
		 * Luego se valida la lógica del cálculo del total a pagar dependiendo
		 * del día de la semana
		 */
		int horaInicial = fechaHoraInicial.get(Calendar.HOUR_OF_DAY);
		int horaFinal = fechaHoraFinal.get(Calendar.HOUR_OF_DAY);

		/*
		 * Luego se valida que la hora inicial sea menor a la final
		 */
		if (horaInicial > horaFinal) {
			return false;
		}

		/*
		 * Variables para contabilizar el tiempo incurrido en cada tarifa
		 */
		long minutosTarifa1 = 0;
		long minutosTarifa2 = 0;
		long minutosTarifa3 = 0;
		long minutosTarifa4 = 0;

		/*
		 * Variables locales para saber cuanto vale cada tarifa
		 */
		int tarifa1 = Integer.parseInt(properties.getProperty("tarifa1"));
		int tarifa2 = Integer.parseInt(properties.getProperty("tarifa2"));
		int tarifa3 = Integer.parseInt(properties.getProperty("tarifa3"));
		int tarifa4 = Integer.parseInt(properties.getProperty("tarifa4"));

		/*
		 * Variables para saber los límites inferiores de cada intervalo de
		 * tiempo de cada tarifa
		 */
		int tarifa1LunesHoraInicial = Integer.parseInt(properties
				.getProperty("tarifa1LunesHoraInicial"));
		int tarifa1MVHoraInicial = Integer.parseInt(properties
				.getProperty("tarifa1MVHoraInicial"));
		int tarifa2LunesHoraInicial = Integer.parseInt(properties
				.getProperty("tarifa2LunesHoraInicial"));
		int tarifa2FinDeSemanaYFestivosHoraInicial = Integer
				.parseInt(properties
						.getProperty("tarifa2FinDeSemanaYFestivosHoraInicial"));
		int tarifa3MVHoraInicial = Integer.parseInt(properties
				.getProperty("tarifa3MVHoraInicial"));
		int tarifa4FinDeSemanaYFestivosHoraInicial = Integer
				.parseInt(properties
						.getProperty("tarifa4FinDeSemanaYFestivosHoraInicial"));

		switch (diaDeLaSemana) {
		case Calendar.MONDAY:
			if (horaInicial < tarifa2LunesHoraInicial) {
				if (horaFinal < tarifa2LunesHoraInicial) {
					// hora inicial y final en tarifa 1
					minutosTarifa1 += (fechaHoraFinal.getTimeInMillis()
							- fechaHoraInicial.getTimeInMillis()) / 1000;
				} else if (horaFinal < 23) {
					// TODO hora inicial en tarifa 1 y hora final en tarifa 2
					
				} else {
					return false; // en caso de que la hora final sea luego de
									// 11
				}
			} else if (horaInicial < 23) {
				if (horaFinal < 23) {
					// TODO hora inicial y final en tarifa 2
				} else {
					return false; // hora final mayor a 11 PM
				}
			} else {
				return false; // en caso de que la hora incial sea después de
								// las 11
			}
			break;
		case Calendar.TUESDAY:
		case Calendar.WEDNESDAY:
		case Calendar.THURSDAY:
		case Calendar.FRIDAY:

			break;
		case Calendar.SATURDAY:
			break;
		case Calendar.SUNDAY:
			break;
		default:
			break;
		}

	}

	private void crearSede() {
		Ciudad ciudad;
		String nombre;
		String direccion;

		// primero se chequea si ya hay ciudades en el sistema
		if (ciudades.size() == 0) {
			System.out
					.println("Aún no se han añaido ciudades al sistema. Adios");
			System.exit(0);
		} else {
			// se pide el nombre de la sede
			System.out.print("Ingrese el nombre de la sede: ");

			nombre = sc.nextLine();

			// se pide la ciudad en la que está ubicada la sede
			System.out
					.println("Ingrese la ciudad en la que está ubicada la sede:");
			int contador = 1;
			for (Ciudad c : ciudades) {
				System.out.println(contador + ". " + c.getNombre());
			}
			System.out.print("Elija una opción [1-" + ciudades.size() + "]: ");

			int opcion = sc.nextInt();
			sc.nextLine();

			ciudad = ciudades.get(opcion - 1);

			// se pide la dirección de la sede
			System.out.print("Ingrese la dirección de la sede: ");

			direccion = sc.nextLine();

			// se crea la sede
			Sede sede = new Sede(nombre, direccion, ciudad);
			sedes.add(sede);

			// se añade la referencia de la sede en la ciuadad
			ciudad.addSede(sede);

			serializar();
			System.out.println("Se creó la sede exitosamente");
		}
	}

	private void eliminarCancha() {
		// primero se muestran las canchas registradas actualmente en el sistema
		System.out.println("Estas son las canchas registradas en el sistema:");
		for (int i = 0; i < canchas.size(); i++) {
			System.out.println((i + 1) + ". " + canchas.get(i).getNombre());
		}

		// luego se pregunta cual cancha se desea elimninar
		System.out.print("Elija una opción [1-" + canchas.size() + "]: ");

		int eleccion = sc.nextInt();
		sc.nextLine();

		// luego se muestra un prompt de confirmación
		System.out.print("Está seguro de que desea eliminar la cancha "
				+ canchas.get(eleccion - 1).getNombre() + "? [s/n]: ");

		String confirmacion = sc.next();

		switch (confirmacion) {
		case "s":
			// se borra la cancha de su sede
			if (canchas.get(eleccion - 1).getSede()
					.eliminarCancha(canchas.get(eleccion - 1).getNombre())) {

				// se borra la cancha de la memoria
				canchas.remove(eleccion - 1);

				// luego se muestra un mensaje de confirmación
				System.out.println("La cancha ha sido borrada exitosamente");

				// se guardan los cambios
				serializar();
			} else {
				System.out.println("Ocurrió un error al borrar la cancha.");
			}
			break;
		case "n":
			System.out.println("No se hicieron cambios.");
			break;
		default:
			System.out
					.println("Ha seleccionado una opción inválida. No se harán cambios. Adios");
			System.exit(0);
			break;
		}
	}

	private void eliminarCiudad() {
		// primero se muestran las ciudades registradas actualmente en el
		// sistema
		System.out.println("Estas son las ciudades registradas en el sistema:");
		for (int i = 0; i < ciudades.size(); i++) {
			System.out.println((i + 1) + ". " + ciudades.get(i).getNombre());
		}

		// luego se pregunta cual ciudad se desea elimninar
		System.out.print("Elija una opción [1-" + ciudades.size() + "]: ");

		int eleccion = sc.nextInt();
		sc.nextLine();

		// luego se muestra un prompt de confirmación
		System.out.print("Está seguro de que desea eliminar la ciudad "
				+ ciudades.get(eleccion - 1).getNombre() + "? [s/n]: ");

		String confirmacion = sc.next();

		switch (confirmacion) {
		case "s":
			// se borran todas las canchas de cada sede de la ciudad
			for (int i = 0; i < sedes.size(); i++) {
				if (sedes.get(i).getCiudad().getNombre()
						.equals(ciudades.get(eleccion - 1).getNombre())) {
					for (int j = 0; j < canchas.size(); j++) {
						if (canchas.get(j).getSede().getNombre()
								.equals(sedes.get(i).getNombre())) {
							canchas.remove(j);
						}
					}
				}
			}
			System.out
					.println("Se eliminaron todas las canchas de cada sede de esta ciudad");

			// se borran todas las sedes de la ciudad
			for (int i = 0; i < sedes.size(); i++) {
				if (sedes.get(i).getCiudad().getNombre()
						.equals(ciudades.get(eleccion - 1).getNombre())) {
					sedes.remove(i);
				}
			}

			System.out
					.println("Se borraron exitosamente todas las sedes de la ciudad.");

			// se borra la ciudad de la memoria
			ciudades.remove(eleccion - 1);

			// luego se muestra un mensaje de confirmación
			System.out.println("La ciudad ha sido borrada exitosamente");

			// se guardan los cambios
			serializar();

			break;
		case "n":
			System.out.println("No se hicieron cambios.");
			break;
		default:
			System.out
					.println("Ha seleccionado una opción inválida. No se harán cambios. Adios");
			System.exit(0);
			break;
		}

	}

	private void eliminarSede() {
		// primero se muestran las sedes registradas actualmente en el sistema
		System.out.println("Estas son las sedes registradas en el sistema:");
		for (int i = 0; i < sedes.size(); i++) {
			System.out.println((i + 1) + ". " + sedes.get(i).getNombre());
		}

		// luego se pregunta cual sede se desea elimninar
		System.out.print("Elija una opción [1-" + sedes.size() + "]: ");

		int eleccion = sc.nextInt();
		sc.nextLine();

		// luego se muestra un prompt de confirmación
		System.out.print("Está seguro de que desea eliminar la sede "
				+ sedes.get(eleccion - 1).getNombre() + "? [s/n]: ");

		String confirmacion = sc.next();

		switch (confirmacion) {
		case "s":
			// se borran todas las canchas de la sede
			for (int i = 0; i < canchas.size(); i++) {
				if (canchas.get(i).getSede().getNombre()
						.equals(sedes.get(eleccion - 1))) {
					canchas.remove(i);
				}
			}

			System.out
					.println("Se borraron exitosamente todas las canchas de la sede.");

			// se borra la sede de su ciudad
			if (sedes.get(eleccion - 1).getCiudad()
					.eliminarSede(sedes.get(eleccion - 1).getNombre())) {

				// se borra la sede de la memoria
				sedes.remove(eleccion - 1);

				// luego se muestra un mensaje de confirmación
				System.out.println("La sede ha sido borrada exitosamente");

				// se guardan los cambios
				serializar();
			} else {
				System.out.println("Ocurrió un error al borrar la sede.");
			}
			break;
		case "n":
			System.out.println("No se hicieron cambios.");
			break;
		default:
			System.out
					.println("Ha seleccionado una opción inválida. No se harán cambios. Adios");
			System.exit(0);
			break;
		}
	}

	private void login() {
		String usuario;
		String password;

		// get the currently executing user:
		currentUser = SecurityUtils.getSubject();

		// pedir usuario y contraseña
		System.out.println("Bienvenido\n");
		System.out.print("Usuario: ");

		usuario = sc.nextLine();

		System.out.print("Contraseña: ");

		password = sc.nextLine();

		// let's login the current user so we can check against roles and
		// permissions:
		if (!currentUser.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken(usuario,
					password);
			token.setRememberMe(true);
			try {
				currentUser.login(token);
			} catch (UnknownAccountException uae) {
				log.info("No existe un usuario con el nombre de usuario "
						+ token.getPrincipal());
				System.exit(0);
			} catch (IncorrectCredentialsException ice) {
				log.info("La contraseña para la cuenta " + token.getPrincipal()
						+ " es incorrecta!");
				System.exit(0);
			} catch (LockedAccountException lae) {
				log.info("La cuenta del usuario "
						+ token.getPrincipal()
						+ " está bloqueada.  "
						+ "Por favor contacte al administrador para desbloquearla.");
				System.exit(0);
			}
			// ... catch more exceptions here (maybe custom ones specific to
			// your application?
			catch (AuthenticationException ae) {
				// unexpected condition? error?
			}

			// say who they are:
			// print their identifying principal (in this case, a username):
			log.info("Usuario [" + currentUser.getPrincipal()
					+ "] inició sesión exitosamente.");

			// test a role:
			if (currentUser.hasRole("schwartz")) {
				log.info("May the Schwartz be with you!");
			} else {
				log.info("Hello, mere mortal.");
			}

			// test a typed permission (not instance-level)
			if (currentUser.isPermitted("lightsaber:weild")) {
				log.info("You may use a lightsaber ring.  Use it wisely.");
			} else {
				log.info("Sorry, lightsaber rings are for schwartz masters only.");
			}

			// a (very powerful) Instance Level permission:
			if (currentUser.isPermitted("winnebago:drive:eagle5")) {
				log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  "
						+ "Here are the keys - have fun!");
			} else {
				log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
			}

			// all done - log out!
			// currentUser.logout();
		}
	}

	private void modificarCancha() {
		// chquear si hay canchas en el sistema
		if (canchas.size() == 0) {
			System.out.println("Aún no se han agregado canchas al sistema.");
			return;
		} else {
			// pedir al usuario que elija la cancha que se desea modificar
			System.out
					.println("Estas son las canchas registradas en el sistema:");
			for (int i = 0; i < canchas.size(); i++) {
				System.out.println((i + 1) + ". " + canchas.get(i).getNombre());
			}
			System.out.print("Elija una opción [1-" + canchas.size() + "]: ");

			int opcionElejida = sc.nextInt();
			sc.nextLine();

			/*
			 * pedir los datos que sean modificables (en este caso sólo el
			 * nombre)
			 */
			System.out.print("Ingrese el nuevo nombre de la cancha: ");

			String nuevoNombre = sc.nextLine();

			// modificar la cancha
			canchas.get(opcionElejida - 1).setNombre(nuevoNombre);

			/*
			 * mostrar mensaje de confirmación de que se hizo el cambio
			 * exitosamente
			 */
			System.out.println("La cancha fue editada correctamente");

			// guardar los cambios
			serializar();
		}
	}

	private void modificarCiudad() {
		// chquear si hay ciudades en el sistema
		if (ciudades.size() == 0) {
			System.out.println("Aún no se han agregado ciudades al sistema.");
			return;
		} else {
			// pedir al usuario que elija la ciudad que se desea modificar
			System.out
					.println("Estas son las ciudades registradas en el sistema:");
			for (int i = 0; i < ciudades.size(); i++) {
				System.out
						.println((i + 1) + ". " + ciudades.get(i).getNombre());
			}
			System.out.print("Elija una opción [1-" + ciudades.size() + "]: ");

			int opcionElejida = sc.nextInt();
			sc.nextLine();

			/*
			 * pedir los datos que sean modificables (en este caso sólo el
			 * nombre)
			 */
			System.out.print("Ingrese el nuevo nombre de la ciudad: ");

			String nuevoNombre = sc.nextLine();

			// modificar la ciudad
			ciudades.get(opcionElejida - 1).setNombre(nuevoNombre);

			/*
			 * mostrar mensaje de confirmación de que se hizo el cambio
			 * exitosamente
			 */
			System.out.println("La ciudad fue editada correctamente");

			// guardar los cambios
			serializar();
		}
	}

	private void modificarSede() {
		// chquear si hay sedes en el sistema
		if (sedes.size() == 0) {
			System.out.println("Aún no se han agregado sedes al sistema.");
			return;
		} else {
			// pedir al usuario que elija la sede que se desea modificar
			System.out
					.println("Estas son las sedes registradas en el sistema:");
			for (int i = 0; i < sedes.size(); i++) {
				System.out.println((i + 1) + ". " + sedes.get(i).getNombre());
			}
			System.out.print("Elija una opción [1-" + sedes.size() + "]: ");

			int opcionElejida = sc.nextInt();
			sc.nextLine();

			/*
			 * pedir los datos que sean modificables (nombre y dirección)
			 */
			System.out.print("Ingrese el nuevo nombre de la sede: ");

			String nuevoNombre = sc.nextLine();

			System.out.print("Ingrese la nueva dirección de la sede: ");

			String nuevaDireccion = sc.nextLine();

			// modificar la sede
			sedes.get(opcionElejida - 1).setNombre(nuevoNombre);
			sedes.get(opcionElejida - 1).setDireccion(nuevaDireccion);

			/*
			 * mostrar mensaje de confirmación de que se hizo el cambio
			 * exitosamente
			 */
			System.out.println("La sede fue editada correctamente");

			// guardar los cambios
			serializar();
		}
	}

	private void montarShiro() {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory(
				"classpath:resources/shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}

	private void mostrarInfraestructura() {
		for (Ciudad c : ciudades) {
			System.out.println("Ciudad: " + c.getNombre());
			for (Sede s : sedes) {
				System.out.println("\tSede: " + s.getNombre() + "("
						+ s.getDireccion() + ")");
				for (Cancha ch : canchas) {
					System.out.println("\tCancha: " + ch.getNombre());
				}
			}
		}
	}

	private void mostrarLogo() {
		// mostrar pantalla de inicio
		try (InputStreamReader isr = new InputStreamReader(new FileInputStream(
				"logo-sistema.txt"), "UTF-8")) {
			int caracter = isr.read();
			while (caracter != -1) {
				System.out.print((char) caracter);
				caracter = isr.read();
			}

			System.out.println("\n\n");
		} catch (FileNotFoundException e) {
			System.out.println("POLITECFUTBOL");
		} catch (IOException ioe) {
			System.out.println("POLITECFUTBOL");
		}
	}

	private void mostrarMenu() {
		boolean salir = false;

		while (!salir) {
			System.out.println("Elija una opción:");

			System.out.println("1. Realizar reserva");
			System.out.println("2. Consultar reserva");

			System.out.println("3. Crear cancha");
			System.out.println("4. Modificar cancha");
			System.out.println("5. Eliminar cancha");

			System.out.println("6. Crear sede");
			System.out.println("7. Modificar sede");
			System.out.println("8. Eliminar sede");

			System.out.println("9. Crear ciudad");
			System.out.println("10. Modificar ciudad");
			System.out.println("11. Eliminar ciudad");

			System.out.println("12. Ver Infraestructura");

			System.out.println("99. Salir del sistema");

			System.out.print("Ingrese una opción [1-12]: ");

			int opcion = sc.nextInt();
			sc.nextLine();

			switch (opcion) {
			case 1:
				crearReserva();
				break;
			case 2:
				consultarReserva();
				break;
			case 3:
				crearCancha();
				break;
			case 4:
				modificarCancha();
				break;
			case 5:
				eliminarCancha();
				break;
			case 6:
				crearSede();
				break;
			case 7:
				modificarSede();
				break;
			case 8:
				eliminarSede();
				break;
			case 9:
				crearCiudad();
				break;
			case 10:
				modificarCiudad();
				break;
			case 11:
				eliminarCiudad();
				break;
			case 12:
				mostrarInfraestructura();
				break;
			case 99:
				System.out.println("El programa se cerrará ahora. Adios");
				System.exit(0);
			}
		}
	}

	/**
	 * Este método se usa para serializar los objetos que hacen parte de la base
	 * de datos de la aplicación.
	 * 
	 * @param canchas2
	 *            El listado de canchas.
	 * @param ciudades2
	 *            El listado de ciudades.
	 * @param clientes2
	 *            El listado de clientes.
	 * @param reservas2
	 *            El listado de reservas.
	 * @param sedes2
	 *            El listado de sedes.
	 */
	private void serializar() {
		try (ObjectOutputStream oosCanchas = new ObjectOutputStream(
				new FileOutputStream(properties.getProperty("rutaCanchas")));
				ObjectOutputStream oosCiudades = new ObjectOutputStream(
						new FileOutputStream(
								properties.getProperty("rutaCiudades")));
				ObjectOutputStream oosClientes = new ObjectOutputStream(
						new FileOutputStream(
								properties.getProperty("rutaClientes")));
				ObjectOutputStream oosSedes = new ObjectOutputStream(
						new FileOutputStream(
								properties.getProperty("rutaSedes")));
				ObjectOutputStream oosReservas = new ObjectOutputStream(
						new FileOutputStream(
								properties.getProperty("rutaReservas")))) {
			oosCanchas.writeObject(canchas);
			oosCiudades.writeObject(ciudades);
			oosClientes.writeObject(clientes);
			oosReservas.writeObject(reservas);
			oosSedes.writeObject(sedes);
			System.out.println("Se guardó la información exitosamente");
		} catch (IOException ioe) {
			System.out.println("Error al salvar los datos");
		}
	}
}
