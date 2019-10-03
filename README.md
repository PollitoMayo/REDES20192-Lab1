# REDES20192-Lab1
UTEM, Redes y comunicación de datos, 2do semestre 2019, Laboratorio 1

**Servidor**

Para iniciar el Servidor se deberán compilar los archivos en primer lugar siguiendo los siguientes comandos en la terminal:
 1. cd src // Para direccionarnos a la carpeta correcta
 2. javac MainServidor.java -d ../bin // Para compilar el archivo Java y destinarlo en la carpeta bin

Una vez compilado se deberán usar los siguientes comandos para poder inicializarla:
 1. cd ../bin // Para direccionarnos a la carpeta correcta.
 2. java MainServidor
Si el numero del puerto no está especificado, por defecto se asignará 8000

**Servidor**

Para iniciar el cliente en la consola se deberán compilar los archivos en primer lugar siguiendo los siguientes comandos:
 1. cd src // Para direccionarnos a la carpeta correcta
 2. javac MainCliente.java -d ../bin // Para compilar el archivo Java y destinarlo en la carpeta bin

Una vez compilado se deberán usar los siguientes comandos para poder inicializarla:
 1. cd ../bin // Para direccionarnos a la carpeta correcta.
 2. java MainCliente

Si no se especifícan los datos, se utilizaran los definidos por defecto:
NombreUsuario: "Anónimo"
NumeroPuerto: 8000
DireccionServidor: "localhost"

**Chat**

En la consola del cliente:
1. Solamente se deberá escribir un mensaje para que llegue a todos los clientes y al servidor.
2. Escribiendo "DESCONECTAR" es posible desconectarse del chat.