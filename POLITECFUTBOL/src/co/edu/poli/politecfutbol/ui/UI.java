package co.edu.poli.politecfutbol.ui;

import java.io.BufferedInputStream;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

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
	private Console c;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new UI();

	}

	public UI() {
		sc = new Scanner(System.in);
		c = System.console();

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

		// Chequear si hay archivos serializados
		// si los hay cargar la información al sistema
		// en caso contrario crear la configuración inicial del sistema

		Factory<SecurityManager> factory = new IniSecurityManagerFactory(
				"classpath:resources/shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);

		login();

		System.exit(0);
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
		Cliente cliente;
		FormaDePago formaDePago;
		Cancha[] canchasAReservar;
		Ciudad ciudad;
		EmpleadoBase empleado;
		
		// pedir info de cliente
		System.out.println("Información del Cliente");
		System.out.print("Ingrese los nombres del cliente: ");
		
		String nombres = sc.next();
		
		System.out.print("Ingrese los apellidos del cliente: ");
		
		String apellidos = sc.next();
		
		System.out.println("Ingrese el tipo de documento de identificación: ");
		System.out.println("1. Cédula");
		System.out.println("2. NIT");
		System.out.println("3. Pasaporte");
		
		int tipoDeDocumento = sc.nextInt();
		TipoDeDocumento docType;
		
		switch(tipoDeDocumento) {
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
		
		System.out.print("Ingrese el número de documento: ");
		
		String numeroDeDocumento = sc.next();
		
		System.out.print("Ingrese el teléfono de contacto: ");
		
		String numeroDeTelefono = sc.next();
		
		System.out.print("Ingrese la dirección del cliente: ");
		
		String direccion = sc.next();
		
		System.out.println("Ingrese el e-mail del cliente");
		
		String email = sc.next();
		
		// pedir info de la reserva
		
		
	}
}
