package sockets ;

import java.util.*;
import java.io.IOException;

public class MainServidor{
    public static void main(String[] args) throws IOException{
        Servidor servidor = new Servidor();
        System.out.println("\n\tIniciando servidor\n");
        servidor.startServer();
    }
}