package com.hotel.services;

import com.hotel.utils.DatabaseConnection;
import java.sql.Connection;
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

    // Método para generar un reporte de ocupación
    public void generateOccupancyReport(LocalDate startDate, LocalDate endDate) {
        String query = "SELECT COUNT(*) AS occupied_rooms FROM reservations WHERE check_in_date <= ? AND check_out_date >= ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(endDate));
            stmt.setDate(2, java.sql.Date.valueOf(startDate));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int occupiedRooms = rs.getInt("occupied_rooms");
                System.out.println("Reporte de ocupación del " + startDate + " al " + endDate +
                                   ": " + occupiedRooms + " habitaciones ocupadas.");
            }
        } catch (SQLException e) {
            System.err.println("Error al generar el reporte de ocupación: " + e.getMessage());
        }
    }
}