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

        // Arrange
        reportService = new ReportService(databaseConnection);

        // Arrange
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    public void testGenerateReport_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("occupied_rooms")).thenReturn(5);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Act
        reportService.generateReport(LocalDate.now(), LocalDate.now().plusDays(3));

        // Assert
        String expectedOutput = "Reporte de ocupación del " + LocalDate.now() + " al " + LocalDate.now().plusDays(3)
                + ": 5 habitaciones ocupadas.";
        assertEquals(outContent.toString().trim(), expectedOutput.trim());

        System.setOut(System.out);

        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGenerateReport_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("Error al ejecutar la consulta"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Act
        reportService.generateReport(LocalDate.now(), LocalDate.now().plusDays(3));

        // Assert
        String expectedOutput = "No se pudo generar el reporte de ocupación.";
        assertEquals(outContent.toString().trim(), expectedOutput.trim());

        System.setOut(System.out);

        verify(preparedStatement, times(1)).executeQuery();
    }
}
