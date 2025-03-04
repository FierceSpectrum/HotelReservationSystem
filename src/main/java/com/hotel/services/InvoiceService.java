package com.hotel.services;

import com.hotel.models.Client;
import com.hotel.models.Reservation;

public class InvoiceService {

    private final ReservationService reservationService;
    private final ClientService clientService;
    private final RoomService roomService;

    // Constructor que recibe instancias de ReservationService, ClientService y RoomService
    public InvoiceService(ReservationService reservationService, ClientService clientService, RoomService roomService) {
        this.reservationService = reservationService;
        this.clientService = clientService;
        this.roomService = roomService;
    }

    public void createInvoice(int reservationId) {
        try {

            Reservation reservation = reservationService.getReservation(reservationId);

            if (reservation != null) {
                Client client = clientService.getClient(reservation.getClientId());
                printInvoice(reservation, client);
            } else {
                System.out.println("No se encontró la reserva con ID: " + reservationId);
            }

        } catch (Exception e) {
            System.err.println("Error al generar la factura: " + e.getMessage());
        }
    }

    private void printInvoice(Reservation reservation, Client client) {
        String invoiceMessage = "Factura generada para la reserva #" + reservation.getId() + "\n"
                + "Cliente: " + client.getName() + "\n"
                + "Total: $" + calculateTotal(reservation);
        System.out.println(invoiceMessage);
    }

    // Método para calcular el total de la factura
    private double calculateTotal(Reservation reservation) {
        double roomPrice = roomService.getRoom(reservation.getRoomId()).getPrice();
        int days = reservation.getCheckInDate().until(reservation.getCheckOutDate()).getDays();
        return roomPrice * days;
    }
}
