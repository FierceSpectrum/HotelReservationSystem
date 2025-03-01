package com.hotel.services;

import com.hotel.models.Client;
import com.hotel.models.Reservation;

public class InvoiceService {

    private final ReservationService reservationService;
    private final ClientService clientService;

    // Constructor que recibe una instancia de DatabaseConnection
    public InvoiceService(ReservationService reservationService, ClientService clientService) {
        this.reservationService = reservationService;
        this.clientService = clientService;
    }

    public void generateInvoice(int reservationId) {
        try {

            Reservation reservation = reservationService.getReservation(reservationId);

            if (reservation != null) {
                Client client = clientService.getClient(reservation.getClientId());

                printInvoice(reservation, client);
            }

        } catch (Exception e) {
            System.err.println("Error al generar la factura: " + e.getMessage());
        }
    }

    private void printInvoice(Reservation reservation, Client client) {
        System.out.println("Factura generada para la reserva #" + reservation.getId());
        System.out.println("Cliente: " + client.getName());
        System.out.println("Total: $" + calculateTotal(reservation));
    }

    // Método para calcular el total de la factura
    private double calculateTotal(Reservation reservation) {
        return 100.0 * reservation.getCheckOutDate().until(reservation.getCheckInDate()).getDays();
    }
}
