package com.hotel.services;

import com.hotel.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReportService {
    private final DatabaseConnection databaseConnection;

    // Constructor que recibe una instancia de DatabaseConnection
    public ReportService(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // Generates a report of the hotel's occupancy
    public int getOccupiedRooms(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(*) AS occupied_rooms FROM reservations WHERE check_in_date <= ? AND check_out_date >= ?";
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas.");
        }

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(endDate));
            stmt.setDate(2, Date.valueOf(startDate));

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("occupied_rooms") : 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el reporte de ocupación: " + e.getMessage());
            return -1;
        }

    }

    public void generateReport(LocalDate startDate, LocalDate endDate) {
        int occupiedRooms = getOccupiedRooms(startDate, endDate);

        if (occupiedRooms >= 0) {
            System.out.println("Reporte de ocupación del " + startDate + " al " + endDate +
                               ": " + occupiedRooms + " habitaciones ocupadas.");
        } else {
            System.err.println("No se pudo generar el reporte de ocupación.");
        }
    }
}
