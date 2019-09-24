import java.io.*;

public class MensajeChat implements Serializable {

	// "Usuarios" recibe la lista de los usuarios conectados
	// "Mensaje" será un mensaje de texto
	// "Desconectar" para salir del chat
	static final int Usuarios = 0, Mensaje = 1, Desconectar = 2;
	private int tipo;
	private String mensaje;
	
	// constructor
	MensajeChat(int tipo, String mensaje) {
		this.tipo = tipo;
		this.mensaje = mensaje;
	}
	
	int getTipo() {
		return tipo;
	}

	String getMensaje() {
		return mensaje;
	}
}