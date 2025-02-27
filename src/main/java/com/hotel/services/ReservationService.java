package com.hotel.services;

import com.hotel.models.Reservation;
import com.hotel.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private final DatabaseConnection databaseConnection;

    // Constructor que recibe una instancia de DatabaseConnection
    public ReservationService() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    // Método para crear una reserva
    public void createReservation(Reservation reservation) {
        String query = "INSERT INTO reservations (client_id, room_id, check_in_date, check_out_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reservation.getClientId());
            stmt.setInt(2, reservation.getRoomId());
            stmt.setDate(3, java.sql.Date.valueOf(reservation.getCheckInDate()));
            stmt.setDate(4, java.sql.Date.valueOf(reservation.getCheckOutDate()));
            stmt.setString(5, reservation.getStatus());
            stmt.executeUpdate();

            // Recuperar el ID generado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    reservation.setId(id);
                    System.out.println("Reserva creada correctamente con ID: " + id);
                } else {
                    throw new SQLException("No se pudo obtener el ID generado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear la reserva: " + e.getMessage());
        }
    }

    // Método para cancelar una reserva
    public void cancelReservation(int id) {
        String query = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Reserva cancelada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al cancelar la reserva: " + e.getMessage());
        }
    }

    // Método para obtener el historial de reservas de un cliente
    public List<Reservation> getReservationHistory(int clientId) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE client_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation(
                    rs.getInt("id"),
                    rs.getInt("client_id"),
                    rs.getInt("room_id"),
                    rs.getDate("check_in_date").toLocalDate(),
                    rs.getDate("check_out_date").toLocalDate(),
                    rs.getString("status")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el historial de reservas: " + e.getMessage());
        }
        return reservations;
    }
}