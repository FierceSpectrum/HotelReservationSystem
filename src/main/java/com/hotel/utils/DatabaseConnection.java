package com.hotel.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {

    // Instancia única de DatabaseConnection (Singleton)
    private static DatabaseConnection instance;
    private HikariDataSource dataSource;

    // Constructor privado para evitar instanciación externa
    private DatabaseConnection() {
       // Método para abrir el pool de conexiones
        openPool();
    }

    // Método para obtener la instancia única (Singleton)
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

    // Método para obtener una conexión del pool
    public Connection getConnection() throws SQLException {
        if (dataSource != null && !dataSource.isClosed()) {
            return dataSource.getConnection();
        } else {
            throw new SQLException("El pool de conexiones está cerrado.");
        }
    }

    // Método para reabrir el pool de conexiones
    public void openPool() {
        try {
            if (dataSource != null && !dataSource.isClosed()) {
                System.out.println("El pool de conexiones ya está abierto.");   
                return;
            }

            // Cargar las propiedades del archivo .env desde el classpath
            Properties env = new Properties();
            InputStream inputStream = DatabaseConnection.class.getClassLoader().getResourceAsStream(".env");
            if (inputStream == null) {
                throw new RuntimeException("No se pudo encontrar el archivo .env en el classpath.");
            }
            env.load(inputStream);
          
            // Configurar HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(env.getProperty("DB_URL"));
            config.setUsername(env.getProperty("DB_USER"));
            config.setPassword(env.getProperty("DB_PASSWORD"));
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);

            // Inicializa el pool de conexiones
            dataSource = new HikariDataSource(config);
            System.out.println("Pool de conexiones configurado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al configurar el poll de conexiones: " + e.getMessage());
        }
    }

    // Método para cerrar el pool de conexiones
    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Pool de conexiones cerrado.");
        }
        else {
            System.out.println("El pool de conexiones ya está cerrado.");
        }
    }

}