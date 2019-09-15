package sockets;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Scanner;

public class Cliente extends Conexion{
    public Cliente(int puerto, String host) throws IOException{
        super(puerto, host);
    }
    public void startClient(String usuario){
        try{
            Scanner scaner = new Scanner(System.in);
            while(true){
                String mensaje = scaner.nextLine();
                Date fecha = new Date();
                Paquete paquete = new Paquete(usuario, mensaje, fecha);
                
                System.out.println(paquete.getNombreDeUsuario() + ": " + paquete.getMensaje() + " [" + paquete.getFecha() + "]");

                ObjectOutputStream salidaServidor = new ObjectOutputStream(socket.getOutputStream());
                salidaServidor.writeObject(paquete);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}