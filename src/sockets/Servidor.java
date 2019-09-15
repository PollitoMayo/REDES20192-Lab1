package sockets;

import java.io.IOException;
import java.io.ObjectInputStream;

public class Servidor extends Conexion{
    public Servidor(int puerto) throws IOException{
        super(puerto);
    }
    public void startServer(){
        try{
            System.out.println("Esperando...");
            while(true){
                socket = servidor.accept();
                System.out.println("Cliente en línea");

                ObjectInputStream salidaCliente = new ObjectInputStream(socket.getInputStream());
                Paquete paquete = (Paquete) salidaCliente.readObject();
                
                System.out.println(paquete.getNombreDeUsuario() + ": " + paquete.getMensaje() + " [" + paquete.getFecha() + "]");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}