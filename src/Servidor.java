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
	
	//Empezar el servidor
	public void start() {
		bool = true;
		//Se crea el socket y se espera por solicitudes de conexión
		try{
			ServerSocket serverSocket = new ServerSocket(puerto);

			// Loop infinito en espera por conexiones
			while(bool){
				Socket socket = serverSocket.accept();
				// Se termina el loop si el server se detiene
				Scanner scaner = new Scanner(System.in);
				String mensaje = scaner.nextLine();
				scaner.close();
				if(mensaje == "CERRAR" || !bool){
					bool = false;
					break;
				}
				// Si un cliente se conecta, se crea un hilo
				HiloCliente cliente = new HiloCliente(socket);
				//Se añade a la lista de clientes
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
				mostrar("Ocurrió una excepción al cerrar el server y los clientes: " + e);
			}
		}
		catch (IOException e) {
            String mensaje = fecha.format(new Date()) + " Ocurrió una excepción en un nuevo socket: " + e + "\n";
			mostrar(mensaje);
		}
		System.out.println("¿Desea crear otro servidor? (S/N)");
		Scanner scaner = new Scanner(System.in);
		String mensaje = scaner.nextLine();
		if(mensaje == "s" || mensaje == "S"){
			System.out.println("Ingrese nuevo puerto");
			puerto = scaner.nextInt();
			Servidor server = new Servidor(puerto);
			server.start();
		}
		scaner.close();
	}
	
	// Mostrar eventos
	private void mostrar(String mensaje) {
		String evento = ">> [" + fecha.format(new Date()) + "] " + mensaje;
		System.out.println(evento);
	}
	
	// Mostrar un mensaje a todos los clientes
	private synchronized boolean broadcast(String texto) {
		mostrar(texto);
		
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
		broadcast(clienteDesconectado + " se ha desconectado");
	}
}