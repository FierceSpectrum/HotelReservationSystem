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
    private int getOccupiedRooms(LocalDate startDate, LocalDate endDate) {
        if (!validateDates(startDate, endDate)) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas y la fecha de inicio debe ser menor que la fecha de fin.");
        }

        String sql = "SELECT COUNT(*) AS occupied_rooms FROM reservations WHERE check_in_date <= ? AND check_out_date >= ?";

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

        String reportMessage = occupiedRooms >= 0 ?
                "Reporte de ocupación del " + startDate + " al " + endDate + ": " + occupiedRooms + " habitaciones ocupadas." :
                "No se pudo generar el reporte de ocupación.";
        System.out.println(reportMessage);
    }

    private boolean validateDates(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null && startDate.isBefore(endDate);
    }
}
