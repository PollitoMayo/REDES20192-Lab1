import java.util.*;

public class MainCliente{
    public static void main(String[] args) {
        int numeroPuerto;
        String direccionServidor, nombreUsuario, respuesta;
        Scanner scaner = new Scanner(System.in);

        //Obtener el nombre de usuario
        System.out.println("Nombre de usuario: ");
        respuesta = scaner.nextLine();
        if(respuesta == ""){
            respuesta = "Anónimo";
        }
        nombreUsuario = respuesta;

        //Obtener la dirección IP
        System.out.println("IP: ");
        respuesta = scaner.nextLine();
        if(respuesta == ""){
            respuesta = "localhost";
        }
        direccionServidor = respuesta;
        
        //Obtener el puerto
        System.out.println("Puerto: ");
        respuesta = scaner.nextLine();
        if(respuesta == ""){
            respuesta = "8000";
        }
        numeroPuerto = Integer.parseInt(respuesta);

        //Se crea el cliente
        Cliente cliente = new Cliente(direccionServidor, numeroPuerto, nombreUsuario);
        if (!cliente.start()){
            //Si no es posible comenzar un cliente se cierra el programa
            System.out.println("No se ha podido establecer conexión");
            scaner.close();
            return;
        }

        System.out.println("\n\tBienvendo al chat.");
        System.out.println("\tPara salir se deberá escribir 'DESCONECTAR'");

        while (true) {
            String mensaje = scaner.nextLine();
            if (mensaje.equalsIgnoreCase("DESCONECTAR")) {
                cliente.enviarMensaje(new MensajeChat(MensajeChat.Desconectar, ""));
                break;
            } else {
                cliente.enviarMensaje(new MensajeChat(MensajeChat.Mensaje, mensaje));
            }
        }
        scaner.close();
        cliente.desconectar();
    }
}