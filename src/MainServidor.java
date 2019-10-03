import java.util.*;

public class MainServidor {
    public static void main(String[] args) {
        Scanner scaner = new Scanner(System.in);
        String respuesta;
        int puerto;

        System.out.println("Puerto: ");
        respuesta = scaner.nextLine();
        scaner.close();
        if(respuesta != ""){
            puerto = Integer.parseInt(respuesta);
        } else {
            puerto = 8000; //Puerto por defecto
        }
        
        System.out.println("\n\tCreando el servidor\n");
        Servidor servidor = new Servidor(puerto);
        
        System.out.println("\n\tEsperando por clientes...\n");
		servidor.start();
	}
}