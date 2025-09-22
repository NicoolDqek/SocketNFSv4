package clienteNFSv4;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("192.168.100.10", 12345)) { 
            System.out.println("Conectado al servidor...");

            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            
        //repeticion
            for (int i = 0; i < 2; i++) {
                System.out.print("Ingrese el número de teléfono: ");
                String telefono = scanner.nextLine();

               
                salida.println(telefono);

                
                String respuesta;
                while ((respuesta = entrada.readLine()) != null) {
                    System.out.println(respuesta);
                   
                    
                    if (respuesta.startsWith("Ciudad:") || 
                        respuesta.equals("Persona dueña de ese número telefónico no existe.")) {
                        break;
                    }
                }
            }

         
            salida.println("salir");

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
