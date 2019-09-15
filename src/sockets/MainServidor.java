package sockets ;

import java.util.*;
import java.io.IOException;

public class MainServidor{
    public static void main(String[] args) throws IOException{
        Scanner scaner = new Scanner(System.in);
        System.out.println("Ingrese puerto");
        int puerto = Integer.parseInt(scaner.nextLine());

        Servidor servidor = new Servidor(puerto);
        System.out.println("\n\tIniciando servidor\n");
        servidor.startServer();
    }
}