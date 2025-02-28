package com.hotel.services;

import com.hotel.models.Reservation;
import com.hotel.utils.DatabaseConnection;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

class NotificationServiceTest {

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeMethod
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Asegurar que el servicio usa el mock de DatabaseConnection
        notificationService = new NotificationService(databaseConnection);

        // Configurar el comportamiento de los mocks
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    void testGetUpcomingCheckIns_Success() throws SQLException {
        // Configurar el mock para executeQuery
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("client_id")).thenReturn(1);
        when(resultSet.getInt("room_id")).thenReturn(1);
        when(resultSet.getDate("check_in_date")).thenReturn(java.sql.Date.valueOf(LocalDate.now().plusDays(1)));
        when(resultSet.getDate("check_out_date")).thenReturn(java.sql.Date.valueOf(LocalDate.now().plusDays(3)));
        when(resultSet.getString("status")).thenReturn("Active");

        // Obtener reservas próximas al check-in
        List<Reservation> reservations = notificationService.getUpcomingCheckIns(2);

        // Verificar que la lista no esté vacía
        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());

        // Verificar que se llamó a executeQuery
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    void testSendNotifications_Success() throws SQLException {
        // Configurar el mock para executeQuery
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("client_id")).thenReturn(1);
        when(resultSet.getInt("room_id")).thenReturn(1);
        when(resultSet.getDate("check_in_date")).thenReturn(java.sql.Date.valueOf(LocalDate.now().plusDays(1)));
        when(resultSet.getDate("check_out_date")).thenReturn(java.sql.Date.valueOf(LocalDate.now().plusDays(3)));
        when(resultSet.getString("status")).thenReturn("Active");

        // Enviar notificaciones
        notificationService.sendNotifications(2);

        // Verificar que se llamó a executeQuery
        verify(preparedStatement, times(1)).executeQuery();
    }
}