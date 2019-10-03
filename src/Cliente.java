import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Cliente {
	// Entradas/Salidas
	private ObjectInputStream sEntrada; // Lee el socket
	private ObjectOutputStream sSalida; // Escribe en el socket
	private Socket socket;

	private String direccionServidor, nombreUsuario, mensaje, respuesta;
	private int numeroPuerto;
	private Scanner scaner;
	private Boolean bool;

	// Constructor
	Cliente(String servidor, int puerto, String usuario) {
		this.direccionServidor = servidor;
		this.numeroPuerto = puerto;
		this.nombreUsuario = usuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String usuario) {
		this.nombreUsuario = usuario;
	}

	// Comienza el chat y retorna si es que hubo algún problema
	public boolean start() {
		try {
			socket = new Socket(direccionServidor, numeroPuerto);
		}
		// Si falla la conexión
		catch (Exception ec) {
			mensaje = "Error conectando con el servidor";
			mostrar(mensaje, 2);
			return false;
		}

		String mensaje = "Conexión aceptada " + socket.getInetAddress() + ":" + socket.getPort();
		mostrar(mensaje, 1);

		// Creando ambos Data Stream
		try {
			sEntrada = new ObjectInputStream(socket.getInputStream());
			sSalida = new ObjectOutputStream(socket.getOutputStream());
			//#
			System.out.println("Se crean los streams");
		} catch (IOException eIO) {
			mensaje = "Hubo una excepción al crear los Streams";
			mostrar(mensaje, 2);
			return false;
		}

		// Se crea un hilo para escuchar desde el servidor
		new Escuchar().start();
		try {
			sSalida.writeObject(nombreUsuario);
		} catch (IOException eIO) {
			mensaje = "Excepción al loggear";
			mostrar(mensaje, 2);
			desconectar();
			return false;
		}
		return true;
	}

	// Mostrar un mensaje en la consola
	private void mostrar(String mensaje, int tipo) {
		//Tipo 0: Mensaje
		//Tipo 1: Conexión aceptada
		//Tipo 2: Error
		if(tipo == 0){
			System.out.println(mensaje);
		}
		if(tipo == 1){
			System.out.println(mensaje);
		}
		if(tipo == 2){
			System.out.println(mensaje);
		}
	}

	// Enviar un mensaje al servidor
	void enviarMensaje(MensajeChat mensaje) {
		try {
			sSalida.writeObject(mensaje);
		} catch (IOException e) {
			this.mensaje = "Hubo un error al escribir en el servidor";
			mostrar(this.mensaje, 2);
		}
	}

	// Si algo sale mal se cierran las entradas, salidas y se desconecta
	public void desconectar() {
		try {
			if (sEntrada != null)
				sEntrada.close();
		} catch (Exception e) {}
		try {
			if (sSalida != null)
				sSalida.close();
		} catch (Exception e) {}
		try {
			if (socket != null)
				socket.close();
		} catch (Exception e) {}
	}

	class Escuchar extends Thread {
		public void run() {
			String mensaje;
			while (true) {
				try {
					mensaje = (String) sEntrada.readObject();
					mostrar(mensaje, 1);
				} catch (IOException e) {
					mensaje = "Se ha caído el servidor";
					mostrar(mensaje, 2);
				} catch (ClassNotFoundException e2) {
				}
			}
		}
	}
}