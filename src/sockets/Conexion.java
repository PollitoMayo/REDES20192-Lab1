package sockets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Esta clase solo almacena datos como el host, puerto y el flujo de datos
public class Conexion{
    private final int PUERTO = 3000;
    private final String HOST = "localhost";
    protected String mensajeServidor;
    protected ServerSocket servidorSocket; //Socket para el servidor
    protected Socket clienteSocket; //Socket para el cliente
    protected DataOutputStream salidaServidor, salidaCliente; //Flujo de datos

    public Conexion(String tipo) throws IOException{
        if(tipo.equalsIgnoreCase("servidor")){
            servidorSocket = new ServerSocket(PUERTO);
            clienteSocket = new Socket();
        } else {
            clienteSocket = new Socket (HOST, PUERTO);
        }
    }
}