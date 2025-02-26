package com.hotel.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Constructor privado para evitar instanciación externa
    private DatabaseConnection() {
        try {
            // Cargar las propiedades del archivo .env desde el classpath
            Properties env = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(".env");
            if (inputStream == null) {
                throw new RuntimeException("No se pudo encontrar el archivo .env en el classpath.");
            }
            env.load(inputStream);

            // Obtener las credenciales de la base de datos
            String url = env.getProperty("DB_URL");
            String user = env.getProperty("DB_USER");
            String password = env.getProperty("DB_PASSWORD");

            // Establecer la conexión
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión a la base de datos establecida.");
        } catch (SQLException | IOException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    // Método para obtener la instancia única de la clase
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    // Método para obtener la conexión
    public Connection getConnection() {
        return connection;
    }

    // Método para cerrar la conexión
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión a la base de datos cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}