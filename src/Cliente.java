import java.net.*;
import java.io.*;
import java.util.*;

public class Cliente {
	// Entradas/Salidas
	private ObjectInputStream sEntrada; // Lee el socket
	private ObjectOutputStream sSalida; // Escribe en el socket
	private Socket socket;

	private String servidor, nombreUsuario;
	private int puerto;

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	// Constructor
	Cliente(String servidor, int puerto, String nombreUsuario) {
		this.servidor = servidor;
		this.puerto = puerto;
		this.nombreUsuario = nombreUsuario;
	}

	// "start" comienza el chat
	public boolean start() {
		try {
			socket = new socket(servidor, puerto);
		}
		// Si falla la conexión
		catch (Exception ec) {
			mostrar("Error conectando con el servidor");
			return false;
		}

		String mensaje = "Conexión aceptada " + socket.getInetAddress() + ":" + socket.getPort();
		mostrar(mensaje);

		// Creando ambos Data Stream
		try {
			sEntrada = new ObjectInputStream(socket.getInputStream());
			sSalida = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException eIO) {
			mostrar("Hubo una excepción al crear los Streams");
			return false;
		}

		// Se crea un hilo para escuchar desde el servidor
		new Escuchar().run();
		try {
			sSalida.writeObject(nombreUsuario);
		} catch (IOException eIO) {
			mostrar("Excepción al loggear");
			desconectar();
			return false;
		}
		return true;
	}

	// Mostrar un mensaje en la consola
	private void mostrar(String mensaje) {
		System.out.println(mensaje);
	}

	// Enviar un mensaje al servidor
	void enviarMensaje(MensajeChat mensaje) {
		try {
			sSalida.writeObject(mensaje);
		} catch (IOException e) {
			mostrar("Hubo un error al escribir en el servidor");
		}
	}

	// Si algo sale mal se cierran las entradas, salidas y se desconecta
	private void desconectar() {
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
}