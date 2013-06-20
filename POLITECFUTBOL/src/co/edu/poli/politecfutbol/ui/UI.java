package co.edu.poli.politecfutbol.ui;

import java.io.BufferedInputStream;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase se encarga de mostrar la interfaz interactiva por línea de
 * comandos
 * 
 * @author apaternina
 * 
 */
public class UI {
	
	private static final transient Logger log = LoggerFactory.getLogger(UI.class);
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
				"logo-sistema.txt"))) {
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

		login();
		
		System.exit(0);
	}

	private void login() {
		 Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
	        SecurityManager securityManager = factory.getInstance();
	        SecurityUtils.setSecurityManager(securityManager);


	        // get the currently executing user:
	        Subject currentUser = SecurityUtils.getSubject();
	}
}
