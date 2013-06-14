package co.edu.poli.politecfutbol.ui;

import java.io.BufferedInputStream;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Esta clase se encarga de mostrar la interfaz interactiva por línea de
 * comandos
 * 
 * @author apaternina
 * 
 */
public class UI {
	
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
	}

	private void login() {
		String usuario;
		String password;
		
		System.out.print("Ingrese su usuario: ");
		usuario = sc.nextLine().trim();
		
		System.out.print("Ingrese su contraseña: ");
		password = sc.nextLine().trim();
		
		if (usuario.equals("admin") && password.equals("admin")) {
			System.out.println("OK");
		} else {
			System.out.println("WRONG");
		}
	}
}
