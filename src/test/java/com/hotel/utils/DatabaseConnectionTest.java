package com.hotel.utils;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {
    
    @Test
    void testDatabaseConnection() {
        // Obtener una conexión del pool
        try ( Connection connection = DatabaseConnection.getConnection()) {
            // Verificar que la coneción no sea nula
            assertNotNull(connection, "La conexión a la base de datos no debe ser nula.");
            
            // Verificar que la coneción esté abierta y sea válida
            assertNotNull(connection.isClosed(), "La conexión debe estar abierta.");
        } catch (SQLException e) {
            fail("Error al obtener la conexión: " + e.getMessage());
        }
    }
}
