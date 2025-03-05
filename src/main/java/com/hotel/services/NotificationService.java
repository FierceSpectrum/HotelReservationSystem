package com.hotel.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hotel.models.Reservation;
import com.hotel.utils.DatabaseConnection;

public class NotificationService {

    private final DatabaseConnection databaseConnection;

    public NotificationService(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // Envía notificaciones a las clientes para los próximos check-ins
    public void createNotifications(int dayBefore) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE check_in_date >= ? AND check_in_date <= ?";
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(dayBefore);
        System.out.println("Buscando reservas entre " + today + " y " + futureDate);

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(today));
            stmt.setDate(2, java.sql.Date.valueOf(futureDate));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        rs.getInt("room_id"),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getDate("check_out_date").toLocalDate(),
                        rs.getString("status"));
                reservations.add(reservation);

            }

            reservations.parallelStream()
                    .forEach(this::sendNotification);
        } catch (SQLException e) {
            System.err.println("Error al obtener reservas próximas al check-in: " + e.getMessage());
        }
    }

    private void sendNotification(Reservation reservation) {
        String message = "Notificación enviada al cliente " + reservation.getClientId() + " para la reserva " +
                reservation.getId() + " con fecha del check-in: " + reservation.getCheckInDate();
        System.out.println(message);
    }
}
