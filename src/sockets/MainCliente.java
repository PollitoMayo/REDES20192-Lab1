package sockets;

import java.io.IOException;
import java.util.*;

public class MainCliente{
    public static void main(String[] args) throws IOException{
        Scanner scaner = new Scanner(System.in);
        System.out.println("Ingrese puerto");
        int puerto = Integer.parseInt(scaner.nextLine());
        System.out.println("Ingrese IP");
        String host = scaner.nextLine();
        System.out.println("Ingrese nombre de usuario");
        String usuario = scaner.nextLine();

        Cliente cliente = new Cliente(puerto, host);
        System.out.println("\n\tIniciando cliente\n");
        cliente.startClient(usuario);
    }
}