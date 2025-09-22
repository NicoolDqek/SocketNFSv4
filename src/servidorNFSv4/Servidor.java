package servidorNFSv4;
import java.io.*;
import java.net.*;
import java.sql.*;

public class Servidor {
    public static void main(String[] args) {
        try (ServerSocket servidorSocket = new ServerSocket(12345)) {
            System.out.println("Servidor esperando conexiones...");

            while (true) {
                try (Socket socketCliente = servidorSocket.accept()) {
                    System.out.println("Cliente conectado: " + socketCliente.getInetAddress());

                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                    PrintWriter salida = new PrintWriter(socketCliente.getOutputStream(), true);

                    String telefono;
                  
                    
                    while ((telefono = entrada.readLine()) != null) {
                        if (telefono.equalsIgnoreCase("salir")) {
                            break; // termino
                        }

                        System.out.println("Consulta recibida para el teléfono: " + telefono);

                        try (Connection conexion = DriverManager.getConnection(
                                "jdbc:mariadb://localhost:3306/personasdb", "root", "Nqek123*")) {

                            Statement declaracion = conexion.createStatement();
                            
                            
                            // 🔹 Consulta con JOIN
                            
                            String sql = "SELECT p.dir_tel, p.dir_nombre, p.dir_direccion, c.ciud_nombre " +
                                         "FROM personas p " +
                                         "JOIN ciudades c ON p.dir_ciud_id = c.ciud_id " +
                                         "WHERE p.dir_tel = '" + telefono + "'";

                            ResultSet resultado = declaracion.executeQuery(sql);

                            if (resultado.next()) {
                                String nombre = resultado.getString("dir_nombre");
                                String direccion = resultado.getString("dir_direccion");
                                String ciudad = resultado.getString("ciud_nombre"); 
                                

                                salida.println("Número de teléfono: " + telefono);
                                salida.println("Nombre: " + nombre);
                                salida.println("Dirección: " + direccion);
                                salida.println("Ciudad: " + ciudad);
                            } else {
                                salida.println("Persona dueña de ese número telefónico no existe.");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            salida.println("Error en la base de datos.");
                        }
                    }

                    System.out.println("Cliente desconectado.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

