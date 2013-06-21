package co.edu.poli.politecfutbol.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
import co.edu.poli.politecfutbol.entidades.EmpleadoBase;
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
	private Scanner sc;
	private Properties properties;
	private ArrayList<Cancha> canchas;
	private ArrayList<Ciudad> ciudades;
	private ArrayList<Cliente> clientes;
	private ArrayList<Reserva> reservas;
	private ArrayList<Sede> sedes;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new UI();

	}

	public UI() {
		sc = new Scanner(System.in);

		mostrarLogo();

		cargarParametrosDelSistema();

		cargarDatos();

		montarShiro();

		login();

		System.exit(0);
	}

	private void montarShiro() {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory(
				"classpath:resources/shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}

	private void cargarParametrosDelSistema() {
		// cargar archivo de propiedades
		properties = new Properties();
		try (FileInputStream fis = new FileInputStream(
				"/src/properties/systemparameters.properties")) {
			properties.load(fis);
		} catch (IOException ioe) {
			System.out
					.println("Error al cargar el archivo de parámetros del sistema");
			System.exit(0);
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
			ciudades = (ArrayList<Ciudad>) oisCanchas.readObject();
			clientes = (ArrayList<Cliente>) oisCanchas.readObject();
			reservas = (ArrayList<Reserva>) oisCanchas.readObject();
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
			System.out
					.println("Ocurrió un error desconocido al leer la base de datos.");
			System.out
					.println("¿Desea crear una nueva base de datos? (nota: se perderán todos los datos salvados previamente");
			System.out.print("Elija una opción [s/n]: ");

			String opcion = sc.next();

			switch (opcion) {
			case "s":
				System.out
						.println("Se procederá a crear la base de datos inicial");
				crearBaseDeDatosInicial();
				break;
			case "n":
				System.out
						.println("Se cerrará la aplicación. Por favor contacte al administrador");
				break;
			default:
				System.out
						.println("Se ha seleccionado una opción inválida. No se harán cambios a la aplicación.");
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
		serializar(canchas, ciudades, clientes, reservas, sedes);
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
	private void serializar(ArrayList<Cancha> canchas2,
			ArrayList<Ciudad> ciudades2, ArrayList<Cliente> clientes2,
			ArrayList<Reserva> reservas2, ArrayList<Sede> sedes2) {
		try (ObjectOutputStream oos = new ObjectOutputStream(FileOutputStream fos = new FileOutputStream())) {
			
		}
	}

	private void login() {
		String usuario;
		String password;

		// get the currently executing user:
		Subject currentUser = SecurityUtils.getSubject();

		// pedir usuario y contraseña
		System.out.println("Bienvenido\n");
		System.out.print("Usuario: ");

		usuario = sc.next();

		System.out.print("Contraseña: ");

		password = sc.next();

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
			} catch (IncorrectCredentialsException ice) {
				log.info("La contraseña para la cuenta " + token.getPrincipal()
						+ " es incorrecta!");
			} catch (LockedAccountException lae) {
				log.info("La cuenta del usuario "
						+ token.getPrincipal()
						+ " está bloqueada.  "
						+ "Por favor contacte al administrador para desbloquearla.");
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
			currentUser.logout();
		}
	}

	public void realizarReserva() {
		/*
		 * Variables locales necesarias para realizar la reserva
		 */
		Cliente cliente;
		FormaDePago formaDePago;
		Cancha[] canchasAReservar;
		Ciudad ciudad;
		EmpleadoBase empleado;

		String primerNombre;
		String segundoNombre;
		String primerApellido;
		String segundoApellido;
		TipoDeDocumento docType;
		String numeroDeDocumento;
		String numeroDeTelefono;
		String direccion;
		String email;

		/*
		 * Sección de datos del Cliente
		 */
		System.out.println("Información del Cliente");

		// se pide el primer nombre
		System.out.print("Ingrese el primer nombre del cliente: ");

		primerNombre = sc.next();

		// se pide el segundo nombre
		System.out.print("Ingrese el segundo nombre del cliente: ");

		segundoNombre = sc.next();

		System.out.print("Ingrese el primer apellido del cliente: ");

		// se pide el primer apellido
		primerApellido = sc.next();

		System.out.print("Ingrese el segundo apellido del cliente: ");

		// se pide el segundo apellido
		segundoApellido = sc.next();

		// se pide el tipo de documento de identificación
		System.out.println("Ingrese el tipo de documento de identificación: ");
		System.out.println("1. Cédula");
		System.out.println("2. NIT");
		System.out.println("3. Pasaporte");

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
		}

		// se pide el número de documento
		System.out.print("Ingrese el número de documento: ");

		numeroDeDocumento = sc.next();

		// se pide el número de teléfono
		System.out.print("Ingrese el teléfono de contacto: ");

		numeroDeTelefono = sc.next();

		// se pide la dirección del cliente
		System.out.print("Ingrese la dirección del cliente: ");

		direccion = sc.next();

		// se pide el email del cliente
		System.out.println("Ingrese el e-mail del cliente");

		email = sc.next();

		// se crea el objeto cliente con toda la información recopilada
		cliente = new Cliente(primerNombre, segundoNombre, primerApellido,
				segundoApellido, docType, numeroDeDocumento, numeroDeTelefono,
				direccion, email);

		/*
		 * Sección de datos de la reserva
		 */
		System.out.println("INFORMACIÓN DE LA RESERVA");

		// se pide la fecha de la reserva
		System.out.print("Ingrese el día de la reserva (DD/MM/AAAA): ");

		String fecha = sc.next();

		StringTokenizer st = new StringTokenizer(fecha, "/");

		int dia = Integer.parseInt(st.nextToken());
		int mes = Integer.parseInt(st.nextToken());
		int year = Integer.parseInt(st.nextToken());

		// se pide la hora inicial de la reserva
		System.out.print("Ingrese la hora inicial ('HH/MM', ej. 14:00): ");

		String horaInicial = sc.next();

		StringTokenizer stHora = new StringTokenizer(horaInicial, "/");

		int horas = Integer.parseInt(st.nextToken());
		int minutos = Integer.parseInt(st.nextToken());

		// se pide la hora final de la reserva
		System.out.print("Ingrese la hora final ('HH/MM', ej. 14:00): ");

		String horaFinal = sc.next();

		StringTokenizer stHoraFinal = new StringTokenizer(horaFinal, "/");

		int horasFinal = Integer.parseInt(st.nextToken());
		int minutosFinal = Integer.parseInt(st.nextToken());

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
	}
}
