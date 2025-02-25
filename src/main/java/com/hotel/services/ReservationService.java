package com.hotel.services;

import com.hotel.models.Client;
import com.hotel.models.Room;
import com.hotel.models.Reservation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {
    private List<Reservation> reservations = new ArrayList<>();
    private RoomService roomService;

    public ReservationService(RoomService roomService) {
        this.roomService = roomService;
    }

    // Books a room if available
    public boolean bookRoom(int reservationId, Client client, Room room, LocalDate checkIn, LocalDate checkOut) {
        if (roomService.checkAvailability(room.getId())) {
            reservations.add(new Reservation(reservationId, client.getId(), room.getId(), checkIn, checkOut));
            roomService.updateAvailability(room.getId(), false);
            return true;
        }
        return false;
    }

    // Cancels an existing reservation and updates room availability
    public boolean cancelReservation(int reservationId) {
        Reservation reservation = reservations.stream()
                .filter(r -> r.getId() == reservationId)
                .findFirst()
                .orElse(null);

        if (reservation != null) {
            roomService.updateAvailability(reservation.getRoomId(), true);
            reservations.remove(reservation);
            return true;
        }
        return false;
    }

    // Retrieves a client's reservation history
    public List<Reservation> getReservationHistory(int clientId) {
        return reservations.stream()
                .filter(r -> r.getClientId() == clientId)
                .collect(Collectors.toList());
    }
}
