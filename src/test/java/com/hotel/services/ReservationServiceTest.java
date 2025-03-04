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

        // Ensure the service uses the mock of DatabaseConnection
        reservationService = new ReservationService(databaseConnection, roomService);

        // Configure the behavior of the mocks
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(connection.prepareStatement(any(), anyInt())).thenReturn(preparedStatement);
    }

    @Test
    public void testCreateReservation_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        // Act
        Reservation reservation = new Reservation(0, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), "Active");
        reservationService.createReservation(reservation);

        // Assert
        assertEquals(reservation.getId(), 1);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testCreateReservation_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Error al crear la reserva"));

        // Act
        Reservation reservation = new Reservation(0, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), "Active");
        reservationService.createReservation(reservation);

        // Assert
        assertEquals(reservation.getId(), 0);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testCancelReservation_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("room_id")).thenReturn(1);

        // Act
        reservationService.cancelReservation(1);

        // Assert
        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement, times(1)).executeQuery();
        verify(roomService, times(1)).updateAvailability(1, true);
    }

    @Test
    public void testCancelReservation_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // Act
        reservationService.cancelReservation(1);

        // Assert
        verify(preparedStatement, times(1)).executeQuery();
        verify(preparedStatement, times(0)).executeUpdate();
        verify(roomService, times(0)).updateAvailability(anyInt(), anyBoolean());
    }

    @Test
    public void testGetReservationHistory_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("client_id")).thenReturn(1);
        when(resultSet.getInt("room_id")).thenReturn(1);
        when(resultSet.getDate("check_in_date")).thenReturn(java.sql.Date.valueOf(LocalDate.now()));
        when(resultSet.getDate("check_out_date")).thenReturn(java.sql.Date.valueOf(LocalDate.now().plusDays(2)));
        when(resultSet.getString("status")).thenReturn("Active");

        // Act
        List<Reservation> reservations = reservationService.getReservationHistory(1);

        // Assert
        assertFalse(reservations.isEmpty());
        assertEquals(reservations.size(), 1);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetReservationHistory_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("Error al obtener el historial de reservas"));

        // Act
        List<Reservation> reservations = reservationService.getReservationHistory(1);

        // Assert
        assertTrue(reservations.isEmpty());
        verify(preparedStatement, times(1)).executeQuery();
    }
}