import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Servidor {
	private static int Id;
	private ArrayList<HiloCliente> listaClientes;
	private SimpleDateFormat fecha;
	private int puerto;
	private boolean bool;
	
	//Constructor
	public Servidor(int puerto) {
		this.puerto = puerto;
		fecha = new SimpleDateFormat("HH:mm:ss");
		listaClientes = new ArrayList<HiloCliente>();
	}
	
	public void start() {
		bool = true;
		//Se crea el socket y se espera por solicitudes de conexión
		try 
		{
			ServerSocket serverSocket = new ServerSocket(puerto);

			// Loop infinito en espera por conexiones
			while(bool){
				Socket socket = serverSocket.accept();
				// Se termina el loop si el server se detiene
				if(!bool)
					break;
				// Si un cliente se conecta, se crea un hilo
				HiloCliente cliente = new HiloCliente(socket);
				//Se añade a la lista de clientes
				listaClientes.add(cliente);
				cliente.start();
			}
			try {
				serverSocket.close();
				for(int i = 0; i < listaClientes.size(); ++i) {
					HiloCliente cliente = listaClientes.get(i);
					try {
					// se cierran los streams
					cliente.sEntrada.close();
					cliente.sSalida.close();
					cliente.socket.close();
					}
					catch(IOException ioE) {
					}
				}
			}
			catch(Exception e) {
				mostrar("Ocurrió una excepción al cerrar el server y los clientes: " + e);
			}
		}
		catch (IOException e) {
            String mensaje = fecha.format(new Date()) + " Ocurrió una excepción en un nuevo socket: " + e + "\n";
			mostrar(mensaje);
		}
	}
	
	// Detener el server
	protected void detener() {
		bool = false;
		try {
			new Socket("localhost", puerto);
		}
		catch(Exception e) {
		}
	}
	
	// Mostrar eventos
	private void mostrar(String mensaje) {
		String evento = fecha.format(new Date()) + " " + mensaje;
		System.out.println(evento);
	}
	
	// Para mostrar un mensaje a todos los clientes
	private synchronized boolean broadcast(String texto) {
		String mensaje = fecha.format(new Date()) + " " + texto + "\n";
		System.out.print(mensaje);
		
		// Se busca si un usuario no se ha desconectado
		for(int i = listaClientes.size(); --i >= 0;) {
			HiloCliente cliente = listaClientes.get(i);
			// Se intenta escribir en el lado del cliente, si falla se remueve de la lista
			if(!cliente.Escribir(mensaje)) {
				listaClientes.remove(i);
				mostrar(cliente.nombreUsuario + " ha sido desconectado.");
			}
		}
		return true;
	}

	// Si el cliente escribió DESCONECTAR
	synchronized void quitar(int id) {
		String clienteDesconectado = "";
		for(int i = 0; i < listaClientes.size(); ++i) {
			HiloCliente cliente = listaClientes.get(i);
			if(cliente.id == id) {
				clienteDesconectado = cliente.getNombreUsuario();
				listaClientes.remove(i);
				break;
			}
		}
		broadcast(clienteDesconectado + " se ha desconectado.");
	}
	
	// Al iniciar el servidor se tiene un valor por defecto para el puerto
	public static void main(String[] args) {
		int puerto = 8000;
		switch(args.length) {
			case 1:
				try {
					puerto = Integer.parseInt(args[0]);
				} catch(Exception e) {
					System.out.println("Número de puerto inválido.");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Argumentos incorrectos");
				return;
		}
		Servidor server = new Servidor(puerto);
		server.start();
	}

	class HiloCliente extends Thread {
		Socket socket;
		ObjectInputStream sEntrada;
		ObjectOutputStream sSalida;
		int id;
		String nombreUsuario;
		MensajeChat mensajeChat;
		String fecha;

		// Constructor
		HiloCliente(Socket socket) {
			id = ++Id;
			this.socket = socket;

			try{
				sSalida = new ObjectOutputStream(socket.getOutputStream());
				sEntrada  = new ObjectInputStream(socket.getInputStream());
				nombreUsuario = (String) sEntrada.readObject();
				broadcast(nombreUsuario + " ha entrado al chat.");
			} catch (IOException e) {
				mostrar("Hubo un problema creando los streams: " + e);
				return;
			} catch (ClassNotFoundException e) {
			}
            fecha = new Date().toString() + "\n";
		}
		
		public String getNombreUsuario() {
			return nombreUsuario;
		}

		public void setNombreUsuario(String nombreUsuario) {
			this.nombreUsuario = nombreUsuario;
		}

		// Loop infinito para mostrar los mensajes
		public void run() {
			boolean bool = true;
			while(bool) {
				try {
					mensajeChat = (MensajeChat) sEntrada.readObject();
				} catch (IOException e) {
					mostrar(nombreUsuario + " ocurrió un problema: " + e);
					break;				
				} catch(ClassNotFoundException e2) {
					break;
				}
				
				String mensaje = mensajeChat.getMensaje();
				switch(mensajeChat.getTipo()) {
				case MensajeChat.Mensaje:
					boolean confirmacion =  broadcast(nombreUsuario + ": " + mensaje);
					if(confirmacion==false){
						String notificacion = "\tSorry. No such user exists.";
						Escribir(notificacion);
					}
					break;
				case MensajeChat.Desconectar:
					mostrar(nombreUsuario + " se ha desconectado.");
					bool = false;
					break;
				}
			}
			quitar(id);
			close();
		}
		
		private void close() {
			try {
				if(sSalida != null) sSalida.close();
			} catch(Exception e) {}
			try {
				if(sEntrada != null) sEntrada.close();
			} catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			} catch (Exception e) {}
		}

		// Se escribe en el lado del cliente
		private boolean Escribir(String mensaje) {
			// Si el cliente está conectado, se escribe
			if(!socket.isConnected()) {
				close();
				return false;
			} try {
				sSalida.writeObject(mensaje);
			}
			// Si ocurre un error se informa al cliente
			catch(IOException e) {
				mostrar("Error al enviar un mensaje a " + nombreUsuario);
			}
			return true;
		}
	}
}