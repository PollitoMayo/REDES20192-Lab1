package sockets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Cliente extends Conexion{
    public Cliente() throws IOException{
        super("cliente");
    }
    public void startClient(){
        try{
            salidaServidor = new DataOutputStream(clienteSocket.getOutputStream());
            for (int  i = 0; i < 2; i++){
                salidaServidor.writeUTF(" " + (i+1));
            }
            clienteSocket.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}