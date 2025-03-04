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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

class ReservationServiceTest {

    @Mock
    private RoomService roomService;

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeMethod
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Asegurar que el servicio usa el mock de DatabaseConnection
        reservationService = new ReservationService(databaseConnection, roomService);

        // Configurar el comportamiento de los mocks
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(connection.prepareStatement(any(), anyInt())).thenReturn(preparedStatement);
    }

    @Test
    public void testCreateReservation() throws SQLException {
        // Configurar el mock para executeUpdate y getGeneratedKeys
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        // Crear una reserva
        Reservation reservation = new Reservation(0, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), "Active");

        // Crear la reserva
        reservationService.createReservation(reservation);

        // Verficar que se asignó un ID
        assertEquals(reservation.getId(), 1);

        // Verificar que se llamó a executeUpdate
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testCancelReservation() throws SQLException {
        // Configurar el mock para executeUpdate
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        // Cancelar la reserva
        reservationService.cancelReservation(1);

        // Verificar que se llamó a executeUpdate
        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetReservationHistory() throws SQLException {
        // Configurar el mock para executeQuery
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("client_id")).thenReturn(1);
        when(resultSet.getInt("room_id")).thenReturn(1);
        when(resultSet.getDate("check_in_date")).thenReturn(java.sql.Date.valueOf(LocalDate.now()));
        when(resultSet.getDate("check_out_date")).thenReturn(java.sql.Date.valueOf(LocalDate.now().plusDays(2)));
        when(resultSet.getString("status")).thenReturn("Active");

        // Obtener el historial de reservas
        List<Reservation> reservations = reservationService.getReservationHistory(1);

        // Verificar que la lista no está vacía
        assertFalse(reservations.isEmpty());
        assertEquals(reservations.size(), 1);

        // Verificar que se llamó a executeQuery
        verify(preparedStatement, times(1)).executeQuery();
    }
}