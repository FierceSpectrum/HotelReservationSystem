package com.hotel.services;

import com.hotel.models.Client;
import com.hotel.models.Room;
import com.hotel.utils.DatabaseConnection;
import com.hotel.models.Reservation;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private final RoomService roomService;

    public ReservationService(RoomService roomService) {
        this.roomService = roomService;
    }

    // Registra una nueva reserva si la habitación está disponible
    public boolean bookRoom(int reservationId, Client client, Room room, LocalDate checkIn, LocalDate checkOut)
            throws SQLException {
        if (!roomService.checkAvailability(room.getId())) {
            return false; // La habitación no está disponible
        }

        String sql = "INSERT INTO reservations (id, client_id, room_id, check_in, check_out) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Inicia transacción

            stmt.setInt(1, reservationId);
            stmt.setInt(2, client.getId());
            stmt.setInt(3, room.getId());
            stmt.setDate(4, Date.valueOf(checkIn));
            stmt.setDate(5, Date.valueOf(checkOut));

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                roomService.updateAvailability(room.getId(), false);
                conn.commit(); // Confirma la transacción
                return true;
            } else {
                conn.rollback(); // Revierte si no se pudo insertar
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Error al reservar habitación", e);
        }
    }

    // Cancela una reserva y actualiza la disponibilidad de la habitación
    public boolean cancelReservation(int reservationId) throws SQLException {
        String sql = "DELETE FROM reservations WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false); // Inicia transacción

            stmt.setInt(1, reservationId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                updateRoomAvailabilityAfterCancellation(conn, reservationId);
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            throw new SQLException("Error al cancelar la reserva", e);
        }
    }

    // Recupera el historial de reservas de un cliente
    public List<Reservation> getReservationHistory(int clientId) throws SQLException {
        String sql = "SELECT * FROM reservations WHERE client_id = ?";
        List<Reservation> history = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation(
                            rs.getInt("id"),
                            rs.getInt("client_id"),
                            rs.getInt("room_id"),
                            rs.getDate("check_in").toLocalDate(),
                            rs.getDate("check_out").toLocalDate());
                    history.add(reservation);
                }
            }
        }
        return history;
    }

    // Método auxiliar para actualizar la disponibilidad de una habitación tras
    // cancelar una reserva
    private void updateRoomAvailabilityAfterCancellation(Connection conn, int reservationId) throws SQLException {
        String getRoomIdSql = "SELECT room_id FROM reservations WHERE id = ?";
        int roomId = -1;

        try (PreparedStatement stmt = conn.prepareStatement(getRoomIdSql)) {
            stmt.setInt(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                }
            }
        }

        if (roomId != -1) {
            roomService.updateAvailability(roomId, true);
        }
    }
}
