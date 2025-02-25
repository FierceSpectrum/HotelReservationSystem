package com.hotel.services;

import com.hotel.models.Reservation;
import com.hotel.models.Room;
import java.util.List;

public class InvoiceService {
    // Generates an invoice for a reservation
    public void generateInvoice(Reservation reservation, List<Room> rooms) {
        Room room = rooms.stream()
                .filter(r -> r.getId() == reservation.getRoomId())
                .findFirst()
                .orElse(null);

        if (room != null) {
            double total = room.getPrice();
            System.out.println("Invoice Generated:");
            System.out.println("Client ID: " + reservation.getClientId());
            System.out.println("Room: " + room.getType());
            System.out.println("Total: $" + total);
        } else {
            System.out.println("Error: Room not found.");
        }
    }
}
