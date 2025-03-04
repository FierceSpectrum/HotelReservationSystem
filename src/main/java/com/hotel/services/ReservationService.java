package com.hotel.services;

import com.hotel.utils.DatabaseConnection;
import com.hotel.models.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private final DatabaseConnection databaseConnection;
    private final RoomService roomService;

    // Constructor que recibe una instancia de DatabaseConnection
    public ReservationService(DatabaseConnection databaseConnection, RoomService roomService) {
        this.databaseConnection = databaseConnection;
        this.roomService = roomService;
    }

    // Registra una nueva reserva si la habitación está disponible
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
                    System.out.println("No se pudo obtener el ID generado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear la reserva: " + e.getMessage());
        }
    }

    // Cancela una reserva y actualiza la disponibilidad de la habitación
    public void cancelReservation(int reservationId) {
        String getRoomSql = "SELECT room_id FROM reservations WHERE id = ?";
        String deleteSql = "DELETE FROM reservations WHERE id = ?";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement getRoomStmt = conn.prepareStatement(getRoomSql);
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            conn.setAutoCommit(false); // Inicia transacción

            // Obtener room_id antes de eliminar la reserva
            getRoomStmt.setInt(1, reservationId);
            int roomId = -1;
            try (ResultSet rs = getRoomStmt.executeQuery()) {
                if (!rs.next()) {
                    System.err.println("No se encontró una habitación para la reservación con ID: " + reservationId);
                    return;
                }
                roomId = rs.getInt("room_id");
            }

            // Eliminar la reserva
            deleteStmt.setInt(1, reservationId);
            int rowsAffected = deleteStmt.executeUpdate();

            if (rowsAffected <= 0) {
                conn.rollback();
                System.err.println("Error al cancelar la reserva " + reservationId);
                return;
            }
            roomService.updateAvailability(roomId, true);
            conn.commit();
            System.out.println("Reserva " + reservationId + " cancelada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al cancelar la reserva: " + e.getMessage());
        }
    }

    // Recupera el historial de reservas de un cliente
    public List<Reservation> getReservationHistory(int clientId) {
        String sql = "SELECT * FROM reservations WHERE client_id = ?";
        List<Reservation> history = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation(
                            rs.getInt("id"),
                            rs.getInt("client_id"),
                            rs.getInt("room_id"),
                            rs.getDate("check_in_date").toLocalDate(),
                            rs.getDate("check_out_date").toLocalDate(),
                            rs.getString("status"));
                    history.add(reservation);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el historial de reservas: " + e.getMessage());
        }
        return history;
    }

    public Reservation getReservation(int reservationId) {
        String sql = "SELECT * FROM reservations WHERE id = ?";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Reservation(
                            rs.getInt("id"),
                            rs.getInt("client_id"),
                            rs.getInt("room_id"),
                            rs.getDate("check_in_date").toLocalDate(),
                            rs.getDate("check_out_date").toLocalDate(),
                            rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar una reservación: " + e.getMessage());
        }
        return null;
    }
}
