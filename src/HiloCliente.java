class HiloCliente extends Thread {
    Socket socket;
    ObjectInputStream sEntrada;
    ObjectOutputStream sSalida;
    int id;
    String nombreUsuario;
    MensajeChat mensajeChat;
    String fecha;

    // Constructor
    HiloCliente(Socket socket) {
        id = ++Id;
        this.socket = socket;

        try{
            sSalida = new ObjectOutputStream(socket.getOutputStream());
            sEntrada  = new ObjectInputStream(socket.getInputStream());
            nombreUsuario = (String) sEntrada.readObject();
            Servidor.broadcast(nombreUsuario + " ha entrado al chat.");
        } catch (IOException e) {
            Servidor.mostrar("Hubo un problema creando los streams");
            return;
        } catch (ClassNotFoundException e) {
        }
        fecha = new Date().toString();
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    // Loop infinito para mostrar los mensajes
    public void run() {
        boolean bool = true;
        while(bool) {
            try {
                mensajeChat = (MensajeChat) sEntrada.readObject();
            } catch (IOException e) {
                Servidor.mostrar("Ocurrió un problema al enviar el mensaje");
                break;				
            } catch(ClassNotFoundException e2) {
                break;
            }
            
            String mensaje = mensajeChat.getMensaje();
            switch(mensajeChat.getTipo()) {
                case MensajeChat.Mensaje:
                    Servidor.broadcast(nombreUsuario + ": " + mensaje);
                    break;
                case MensajeChat.Desconectar:
                    bool = false;
                    break;
            }
        }
        Servidor.quitar(id);
        close();
    }
    
    private void close() {
        try {
            if(sSalida != null) sSalida.close();
        } catch(Exception e) {}
        try {
            if(sEntrada != null) sEntrada.close();
        } catch(Exception e) {};
        try {
            if(socket != null) socket.close();
        } catch (Exception e) {}
    }

    // Se escribe en el lado del cliente
    private boolean Escribir(String mensaje) {
        // Si el cliente está conectado, se escribe
        if(!socket.isConnected()) {
            close();
            return false;
        } try {
            sSalida.writeObject(mensaje);
        }
        // Si ocurre un error se informa al cliente
        catch(IOException e) {
            Servidor.mostrar("Error al enviar el mensaje");
        }
        return true;
    }
}