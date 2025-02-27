package com.hotel.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {
    private Reservation reservation;
    private LocalDate checkIn;
    private LocalDate checkOut;

    @BeforeEach
    void setUp() {
        checkIn = LocalDate.of(2023, 10, 1);
        checkOut = LocalDate.of(2023, 10, 5);
        reservation = new Reservation(1, 101, 201, checkIn, checkOut, "Active");
    }

    @Test
    void testReservationConstructor() {
        assertEquals(1, reservation.getId());
        assertEquals(101, reservation.getClientId());
        assertEquals(201, reservation.getRoomId());
        assertEquals(checkIn, reservation.getCheckInDate());
        assertEquals(checkOut, reservation.getCheckOutDate());
        assertEquals("Active", reservation.getStatus());
    }

    @Test
    void testReservationSetters() {
        reservation.setId(2);
        reservation.setClientId(102);
        reservation.setRoomId(202);
        reservation.setCheckInDate(LocalDate.of(2023, 11, 1));
        reservation.setCheckOutDate(LocalDate.of(2023, 11, 5));
        reservation.setStatus("Cancelled");

        assertEquals(2, reservation.getId());
        assertEquals(102, reservation.getClientId());
        assertEquals(202, reservation.getRoomId());
        assertEquals(LocalDate.of(2023, 11, 1), reservation.getCheckInDate());
        assertEquals(LocalDate.of(2023, 11, 5), reservation.getCheckOutDate());
        assertEquals("Cancelled", reservation.getStatus());
    }
}