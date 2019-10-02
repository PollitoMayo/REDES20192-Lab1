import java.net.*;
import java.io.*;
import java.util.*;

class Escuchar extends Thread {
    public void run() {
        while (true) {
            try {
                String mensaje = (String) sEntrada.readObject();
                Cliente.mostrar(">>" + mensaje);
            } catch (IOException e) {
                Cliente.mostrar("Se ha caído el servidor");

                Scanner scaner = new Scanner(System.in);
                System.out.println("Ingrese una nueva IP: ");
                String respuesta = scaner.netxLine();
                if (respuesta == ""){
                    respuesta = "localhost";
                }
                String servidor = respuesta;

                System.out.println("Ingrese un nuevo puerto: ");
                String respuesta = scaner.netxLine();
                if (respuesta == ""){
                    respuesta = "8000";
                }
                int puerto = Integer.parseInt(respuesta);
                scaner.close();
                System.out.println("\n\tReconectando...");
                //Cliente cliente = new Cliente(servidor, puerto);
                break;
            } catch (ClassNotFoundException e2) {
            }
        }
    }
}