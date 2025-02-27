package com.hotel.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hotel.models.Reservation;
import com.hotel.utils.DatabaseConnection;

public class InvoiceService {

    public void generateInvoice(Reservation reservation) {
        String query = "SELECT type, price FROM rooms WHERE id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, reservation.getRoomId());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String roomType = resultSet.getString("type");
                double total = resultSet.getDouble("price");

                System.out.println("Invoice Generated:");
                System.out.println("Client ID: " + reservation.getClientId());
                System.out.println("Room: " + roomType);
                System.out.println("Total: $" + total);
            } else {
                System.out.println("Error: Room not found.");
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
