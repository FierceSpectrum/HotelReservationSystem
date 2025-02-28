package com.hotel.controllers;

import com.hotel.models.Reservation;
import com.hotel.services.ReservationService;
import java.time.LocalDate;
import java.util.List;

public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Crear una reserva
    public void createReservation(int clientId, int roomId, LocalDate checkIn, LocalDate checkOut, String satus) {
        Reservation reservation = new Reservation(0, clientId, roomId, checkIn, checkOut, satus);
        reservationService.createReservation(reservation);
        System.out.println("Reserva creada correctamente");
    }

    // Cancelar una reserva
    public void cancelReservation(int id) {
        reservationService.cancelReservation(id);
        System.out.println("Reserva cancelada correctamente");
    }

    // Obtener el historial de reservas de un cliente
    public List<Reservation> getClientReservations(int clientId) {
        return reservationService.getReservationHistory(clientId);
    }
}
