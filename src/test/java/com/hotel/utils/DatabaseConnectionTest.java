package com.hotel.utils;

import org.testng.annotations.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.testng.Assert.*;

public class DatabaseConnectionTest {
    
    @Test
    void testDatabaseConnection() {
        // Obtener la instancia única de DatabaseConnection
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();

        // Obtener una conexión del pool
        try ( Connection connection = dbConnection.getConnection()) {
            // Verificar que la coneción no sea nula
            assertNotNull(connection, "La conexión a la base de datos no debe ser nula.");
            
            // Verificar que la coneción esté abierta y sea válida
            assertNotNull(connection.isClosed(), "La conexión debe estar abierta.");

            // Cerar la conexión
            connection.close();

            // Verificar que la conexión esté cerrada
            assertTrue(connection.isClosed(), "La conexión debe estar cerrada.");
        } catch (SQLException e) {
            fail("Error al obtener la conexión: " + e.getMessage());
        }
    }
}
