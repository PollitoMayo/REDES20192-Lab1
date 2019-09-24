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
		// Intenta conectar el servidor
		try {
			socket = new Socket(servidor, puerto);
		}
		// Si falla la conexión
		catch (Exception ec) {
			mostrar("Error conectando con el servidor:" + ec);
			return false;
		}

		String mensaje = "Conexión aceptada " + socket.getInetAddress() + ":" + socket.getPort();
		mostrar(mensaje);

		// Creando ambos Data Stream
		try {
			sEntrada = new ObjectInputStream(socket.getInputStream());
			sSalida = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException eIO) {
			mostrar("Hubo una excepción al crear los Streams: " + eIO);
			return false;
		}

		// Se crea un hilo para escuchar desde el servidor
		new Escuchar().start();
		try {
			sSalida.writeObject(nombreUsuario);
		} catch (IOException eIO) {
			mostrar("Excepción al loggear : " + eIO);
			desconectar();
			return false;
		}
		return true;
	}

	// Enviar un mensaje a la consola
	private void mostrar(String mensaje) {
		System.out.println(mensaje);
	}

	// Enviar un mensaje al servidor
	void enviarMensaje(MensajeChat mensaje) {
		try {
			sSalida.writeObject(mensaje);
		} catch (IOException e) {
			mostrar("Excepción escribiendo en el servidor: " + e);
		}
	}

	// Si algo sale mal se cierran las entradas, salidas y se desconecta
	private void desconectar() {
		try {
			if (sEntrada != null)
				sEntrada.close();
		} catch (Exception e) {
		}
		try {
			if (sSalida != null)
				sSalida.close();
		} catch (Exception e) {
		}
		try {
			if (socket != null)
				socket.close();
		} catch (Exception e) {
		}
	}

	// Al iniciar el cliente se tienen definidos por defectos los valores del
	// puerto, IP y nombre de usuario
	public static void main(String[] args) {
		int puerto = 8000;
		String servidor = "localhost";
		String nombreUsuario = "Anónimo";
		Scanner scaner = new Scanner(System.in);

		System.out.println("Nombre de usuario: ");
		nombreUsuario = scaner.nextLine();
		System.out.println("IP: ");
		servidor = scaner.nextLine();
		System.out.println("Puerto: ");
		puerto = Integer.parseInt(scaner.nextLine());

		switch (args.length) {
		case 3:
			// Nombre de usuario, puerto e IP
			servidor = args[2];
		case 2:
			// Nombre de usuario y puerto
			try {
				puerto = Integer.parseInt(args[1]);
			} catch (Exception e) {
				System.out.println("Puerto inválido.");
				return;
			}
		case 1:
			// Nombre de usuario
			nombreUsuario = args[0];
		case 0:
			break;
		// Si la cantidad de argumentos es inválida
		default:
			System.out.println("La cantidad de argumentos es incorrecta");
			return;
		}
		// create the Client object
		Cliente cliente = new Cliente(servidor, puerto, nombreUsuario);
		// Se retorna si no conecta con el servidor
		if (!cliente.start())
			return;

		System.out.println("\nBienvendo al chat.");
		System.out.println("Para salir se deberá escribir 'DESCONECTAR'");

		while (true) {
			System.out.print("> ");

			String mensaje = scaner.nextLine();
			if (mensaje.equalsIgnoreCase("DESCONECTAR")) {
				cliente.enviarMensaje(new MensajeChat(MensajeChat.Desconectar, ""));
				break;
			} else {
				cliente.enviarMensaje(new MensajeChat(MensajeChat.Mensaje, mensaje));
			}
		}
		scaner.close();
		cliente.desconectar();
	}

	// Espera por el mensaje del servidor
	class Escuchar extends Thread {
		public void run() {
			while (true) {
				try {
					String mensaje = (String) sEntrada.readObject();
					System.out.println(mensaje);
					System.out.print("> ");
				} catch (IOException e) {
					mostrar("Se ha cerrado la conexión: " + e);
					break;
				} catch (ClassNotFoundException e2) {
				}
			}
		}
	}
}