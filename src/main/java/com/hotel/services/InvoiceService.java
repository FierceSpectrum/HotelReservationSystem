package com.hotel.services;

import com.hotel.models.Reservation;
import com.hotel.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InvoiceService {

    private final DatabaseConnection databaseConnection;

    // Constructor que recibe una instancia de DatabaseConnection
    public InvoiceService(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // Método para generar una factura
    public void generateInvoice(int reservationId) {
        String query = "SELECT * FROM reservations WHERE id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Reservation reservation = new Reservation(
                    rs.getInt("id"),
                    rs.getInt("client_id"),
                    rs.getInt("room_id"),
                    rs.getDate("check_in_date").toLocalDate(),
                    rs.getDate("check_out_date").toLocalDate(),
                    rs.getString("status")
                );

                // Simular la generación de la factura
                System.out.println("Factura generada para la reserva " + reservation.getId() +
                                   " del cliente " + reservation.getClientId() +
                                   " con un total de $" + calculateTotal(reservation));
            }
        } catch (SQLException e) {
            System.err.println("Error al generar la factura: " + e.getMessage());
        }
    }

    // Método para calcular el total de la factura
    private double calculateTotal(Reservation reservation) {
        // Simular un cálculo basado en el precio de la habitación y la duración de la estadía
        return 100.0 * reservation.getCheckOutDate().until(reservation.getCheckInDate()).getDays();
    }
}