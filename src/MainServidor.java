import java.io.*;
import java.net.*;
import java.util.*;

public class MainServidor {
	private int puerto;

    public getPuerto(){
        return puerto;
    }

    public static void main(String[] args) {
        Scanner scaner = new Scanner(System.in);
        String respuesta;

        System.out.println("Puerto: ");
        respuesta = scaner.nextLine();
        scaner.close();

        if(respuesta != ""){
            puerto = Integer.parseInt(respuesta);
        } else {
            puerto = 8000; //Puerto por defecto
        }
        
        System.out.println("\n\tCreando el servidor\n");
        System.out.println("\n\tEsperando por clientes...\n");
        
		Servidor servidor = new Servidor(puerto);
		servidor.start();
	}
}