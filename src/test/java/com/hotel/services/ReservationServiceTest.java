package com.hotel.services;

import com.hotel.models.Reservation;
import com.hotel.utils.DatabaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

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

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    void testCreateReservation_Success() throws SQLException {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), "Active");
        reservationService.createReservation(reservation);

        verify(preparedStatement, times(1)).executeUpdate();
        System.out.println("Prueba de crear reserva exitosa.");
    }

    @Test
    void testCreateReservation_Failure() throws SQLException {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), "Active");
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Error de base de datos"));

        assertThrows(SQLException.class, () -> reservationService.createReservation(reservation));
        System.out.println("Prueba de crear reserva fallida.");
    }

    @Test
    void testCancelReservation_Success() throws SQLException {
        reservationService.cancelReservation(1);

        verify(preparedStatement, times(1)).executeUpdate();
        System.out.println("Prueba de cancelar reserva exitosa.");
    }

    @Test
    void testCancelReservation_Failure() throws SQLException {
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Error de base de datos"));

        assertThrows(SQLException.class, () -> reservationService.cancelReservation(1));
        System.out.println("Prueba de cancelar reserva fallida.");
    }

    @Test
    void testGetReservationHistory_Success() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("client_id")).thenReturn(1);
        when(resultSet.getInt("room_id")).thenReturn(1);
        when(resultSet.getDate("check_in_date")).thenReturn(java.sql.Date.valueOf(LocalDate.now()));
        when(resultSet.getDate("check_out_date")).thenReturn(java.sql.Date.valueOf(LocalDate.now().plusDays(2)));
        when(resultSet.getString("status")).thenReturn("Active");

        List<Reservation> reservations = reservationService.getReservationHistory(1);
        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
        System.out.println("Prueba de obtener historial de reservas exitosa.");
    }

    @Test
    void testGetReservationHistory_Failure() throws SQLException {
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("Error de base de datos"));

        assertThrows(SQLException.class, () -> reservationService.getReservationHistory(1));
        System.out.println("Prueba de obtener historial de reservas fallida.");
    }
}