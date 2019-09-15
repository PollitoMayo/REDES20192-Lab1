package sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Esta clase solo realiza las conexiones
public class Conexion{
    protected ServerSocket servidor; //Socket para el servidor
    protected Socket socket; //Socket para el cliente

    public Conexion(int puerto) throws IOException{
        servidor = new ServerSocket(puerto);
    }

    public Conexion(int puerto, String host) throws IOException{
        socket = new Socket(host, puerto);
    }
}