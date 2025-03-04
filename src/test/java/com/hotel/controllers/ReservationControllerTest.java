package com.hotel.controllers;

import com.hotel.models.Reservation;
import com.hotel.services.ReservationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reservationController = new ReservationController(reservationService);
    }

    @Test
    public void testCreateReservation() {
        // Arrange
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), "Active");
        doNothing().when(reservationService).createReservation(reservation);

        // Act
        reservationController.createReservation(1, 1, LocalDate.now(), LocalDate.now().plusDays(2), "Active");

        // Assert
        verify(reservationService, times(1)).createReservation(any(Reservation.class));
    }

    @Test
    public void testCancelReservation() {
        // Arrange
        doNothing().when(reservationService).cancelReservation(1);

        // Act
        reservationController.cancelReservation(1);

        // Assert
        verify(reservationService, times(1)).cancelReservation(1);
    }

    @Test
    public void testGetReservationHistory() {
        // Arrange
        List<Reservation> reservations = Arrays.asList(
            new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), "Active"),
            new Reservation(2, 1, 2, LocalDate.now().plusDays(3), LocalDate.now().plusDays(5), "Active")
        );
        when(reservationService.getReservationHistory(1)).thenReturn(reservations);

        // Act
        List<Reservation> result = reservationController.getClientReservations(1);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(result.size(), 2);
    }
}