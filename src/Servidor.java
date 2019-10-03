import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Servidor {
	private static int Id;
	private ArrayList<HiloCliente> listaClientes;
	private SimpleDateFormat fecha;
	private int numeroPuerto;
	private boolean bool;
	private String mensaje;
	
	//Constructor
	public Servidor(int puerto) {
		this.numeroPuerto = puerto;
		fecha = new SimpleDateFormat("HH:mm:ss");
		listaClientes = new ArrayList<HiloCliente>();
	}
	
	//Comienza el servidor
	public void start() {
		bool = true;
		Socket socket;
		Scanner scaner;
		//Se crea el socket y se espera por solicitudes de conexión
		try{
			ServerSocket serverSocket = new ServerSocket(numeroPuerto);
			// Loop infinito en espera por conexiones
			while(bool){
				socket = serverSocket.accept();
				if(!bool){
					System.out.println("Se ha caido el servidor");
					System.out.println("Ingrese un nuevo puerto");
					scaner = new Scanner(System.in);
					numeroPuerto = Integer.parseInt(scaner.nextLine());
					scaner.close();
					serverSocket = new ServerSocket(numeroPuerto);
					socket = serverSocket.accept();
				}
				
				HiloCliente cliente = new HiloCliente(socket);
				listaClientes.add(cliente);
				cliente.start();
			}
			try{
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
				mensaje = "Ocurrió una excepción al cerrar el servidor y los clientes";
				mostrar(mensaje, 1);
			}
		}
		catch (IOException e) {
            mensaje = "Ocurrió una excepción en un nuevo socket";
			mostrar(mensaje, 1);
		}
	}
	
	// Mostrar eventos
	public void mostrar(String mensaje, int tipo) {
		//Tipo 0: Mensaje
		//Tipo 1: Error
		//Tipo 2: Desconectado
		if(tipo == 0){
			System.out.println(">> [" + fecha.format(new Date()) + "] " + mensaje);
		}
		if(tipo == 1){
			System.out.println(mensaje);
		}
		if(tipo == 2){
			System.out.println("\t " + mensaje);
		}
	}
	
	// Mostrar un mensaje a todos los clientes
	private synchronized boolean broadcast(String texto) {
		mostrar(texto, 1);
		
		// Se busca si un usuario no se ha desconectado
		for(int i = listaClientes.size(); --i >= 0;) {
			HiloCliente cliente = listaClientes.get(i);
			// Se intenta escribir en el lado del cliente, si falla se remueve de la lista
			mensaje = fecha.format(new Date()) + " " + texto + "\n";
			if(!cliente.Escribir(mensaje)) {
				listaClientes.remove(i);
				mensaje = cliente.nombreUsuario + " ha sido desconectado.";
				mostrar(mensaje, 2);
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
		broadcast(clienteDesconectado + " se ha desconectado");
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
				mensaje = "Hubo un problema creando los streams";
				mostrar(mensaje, 1);
				return;
			} catch (ClassNotFoundException e) {
			}
			fecha = new Date().toString();
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
					mensaje = "Ocurrió un problema al enviar el mensaje";
					mostrar(mensaje, 1);
					break;				
				} catch(ClassNotFoundException e2) {
					break;
				}
				
				String mensaje = mensajeChat.getMensaje();
				switch(mensajeChat.getTipo()) {
					case MensajeChat.Mensaje:
						broadcast(nombreUsuario + ": " + mensaje);
						break;
					case MensajeChat.Desconectar:
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
				mensaje = "Error al enviar el mensaje";
				mostrar(mensaje, 1);
			}
			return true;
		}
	}
}