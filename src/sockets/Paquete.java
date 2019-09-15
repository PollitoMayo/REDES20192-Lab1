package sockets;

import java.io.Serializable;
import java.util.Date;

public class Paquete implements Serializable{
    private String nombreDeUsuario; 
    private String mensaje;
    private Date fecha;

    Paquete(String nombreDeUsuario, String mensaje, Date fecha){
        this.nombreDeUsuario = nombreDeUsuario;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Date getFecha() {
        return fecha;
    }
}