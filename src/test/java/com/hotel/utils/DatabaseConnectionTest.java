package com.hotel.utils;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.testng.Assert.*;

public class DatabaseConnectionTest {

    private DatabaseConnection dbConnection;

    @BeforeMethod
    public void setUp() {
        // Inicializa el objeto DatabaseConnection antes de que se jecuten las pruebas
        dbConnection = DatabaseConnection.getInstance();

        // Asegúrate de que el pool de conexiones esté abierto
        dbConnection.openPool();
    }

    @AfterMethod
    public void tearDown() {
        // Asegúrar de que se cierre el pool de conexiones despues de cada test
        dbConnection.closePool();
    }

    @Test
    void testDatabaseConnection_Success() {
        // Arrange
        try (Connection connection = dbConnection.getConnection()) {
            // Assert: Verificar que la conexión no sea nula
            assertNotNull(connection, "La conexión a la base de datos no debe ser nula.");

            // Assert: Verificar que la conexión esté abierta y sea válida
            assertFalse(connection.isClosed(), "La conexión debe estar abierta.");

            // Act: Cerrar la conexión
            connection.close();

            // Assert: Verificar que la conexión esté cerrada
            assertTrue(connection.isClosed(), "La conexión debe estar cerrada.");
        } catch (SQLException e) {
            fail("Error al obtener la conexión: " + e.getMessage());
        }
    }

    @Test
    void testDatabaseConnection_Failure() {
        // Arrange
        dbConnection.closePool(); // Asegúrate de que el pool de conexiones esté cerrado

        // Act & Assert
        try (Connection connection = dbConnection.getConnection()) {
            System.out.println("3");
            fail("Se esperaba una SQLException al intentar obtener una conexión con el pool cerrado.");
        } catch (SQLException e) {
            // Assert: Verificar que se lanzó la excepción esperada
            assertTrue(e.getMessage().contains("pool de conexiones está cerrado"),
                    "Se esperaba una SQLException con el mensaje adecuado.");
        }
    }

}
