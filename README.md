# REDES20192-Lab1
UTEM, Redes y comunicación de datos, 2do semestre 2019, Laboratorio 1

**Cliente**

Para iniciar el Cliente se deben usar los siguientes comandos en la consola:
 1. java Cliente
 2. java Cliente NombreUsuario
 3. java Cliente NombreUsuario NumeroPuerto
 4. java Cliente NombreUsuario NumeroPuerto DireccionServidor

Si no se especifícan los datos, se utilizaran los definidos por defecto:
NombreUsuario: "Anónimo"
NumeroPuerto: 8000
DireccionServidor: "localhost"

**Servidor**

Para iniciar el servidor en la consola se deben seguir los siguientes comandos:
 1. java Servidor
 2. java Servidor NumeroPuerto
Si NumeroPuerto no está especificado, por defecto se asignará 8000

**Chat**

En la consola del cliente:
1. Solamente se deberá escribir un mensaje para que llegue a todos los clientes y al servidor.
2. Escribiendo "DESCONECTAR" es posible desconectarse del chat.