package com.hotel.utils;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {
    
    @Test
    void testDatabaseConnection() {
        // Obtener la instancia de DatabaseConnection
        DatabaseConnection db = DatabaseConnection.getInstance();

        // Verificar que la coneción no sea nula
        Connection connection = db.getConnection();
        assertNotNull(connection, "La conexión a la base de datos no debe ser nula.");

        // Cerrar la conexión (opcional)
        db.closeConnection();
    }
}
