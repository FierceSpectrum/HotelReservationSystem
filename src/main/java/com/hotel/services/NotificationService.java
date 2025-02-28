package com.hotel.services;

import com.hotel.models.Reservation;
import com.hotel.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final DatabaseConnection databaseConnection;

    // Constructor que recibe una instancia de DatabaseConnection
    public NotificationService(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // Método para obtener reservas con check-in en los próximos días
    public List<Reservation> getUpcomingCheckIns(int dayBefore) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE check_in_date BETWEEN ? AND ?";
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(dayBefore);

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
        } catch (SQLException e) {
            System.err.println("Error al obtener reservas próximas al check-in: " + e.getMessage());
        }
        return reservations;
    }

    // Método para enviar notificaciones
    public void sendNotifications(int dayBefore) {
        List<Reservation> upcomingReservations = getUpcomingCheckIns(dayBefore);
        for (Reservation reservation : upcomingReservations) {
            System.out.println("Notificación enviada al cliente " + reservation.getClientId() + " para la reserva "
                    + reservation.getId() + " con fecha del check-in: " + reservation.getCheckInDate());
        }
    }
}
