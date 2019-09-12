package sockets;

import java.io.IOException;
import java.util.*;

public class MainCliente{
    public static void main(String[] args) throws IOException{
        Cliente cliente = new Cliente();
        System.out.println("\n\tIniciando cliente\n");
        cliente.startClient();
    }
}