package sockets;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Servidor extends Conexion{
    public Servidor() throws IOException{
        super("servidor");
    }
    public void startServer(){
        try{
            System.out.println("Esperando...");
            clienteSocket = servidorSocket.accept();
            System.out.println("Cliente en línea");

            salidaCliente = new DataOutputStream(clienteSocket.getOutputStream());
            salidaCliente.writeUTF("Petición recibida y aceptada");
            BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            mensajeServidor = entrada.readLine();
            while(mensajeServidor != null){
                System.out.println(mensajeServidor);
                mensajeServidor = entrada.readLine();
            }
            System.out.println("Fin de la conexión");
            servidorSocket.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}