package com.hotel.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {
    private static HikariDataSource dataSource;

    static {
        try {
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

            dataSource = new HikariDataSource(config);
            System.out.println("Pool de conexiones configurado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al configurar el poll de conexiones: " + e.getMessage());
        }
    }

    // Método para obtener una conexión del pool
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Método para cerrar el pool de conexiones
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Pool de conexiones cerrado.");
        }
    }
}