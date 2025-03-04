package com.hotel.services;

import com.hotel.utils.DatabaseConnection;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

class ReportServiceTest {

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ReportService reportService;

    @BeforeMethod
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Asegurar que el servicio usa el mock de DatabaseConnection
        reportService = new ReportService(databaseConnection);

        // Configurar el comportamiento de los mocks
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    public void testGenerateReport() throws SQLException {
        // Configurar el mock para executeQuery
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("occupied_rooms")).thenReturn(5);

        // Capturar la salida impresa en consola
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Ejecutar el método
        reportService.generateReport(LocalDate.now(), LocalDate.now().plusDays(3));

        // Verificar que la salida sea la esperada
        String expectedOutput = "Reporte de ocupación del " + LocalDate.now() + " al " + LocalDate.now().plusDays(3) + ": 5 habitaciones ocupadas.\n";
        assertEquals(outContent.toString().trim(), expectedOutput.trim());

        // Restaurar la salida estándar
        System.setOut(System.out);

        // Verificar que se llamó a executeQuery
        verify(preparedStatement, times(1)).executeQuery();
    }
}