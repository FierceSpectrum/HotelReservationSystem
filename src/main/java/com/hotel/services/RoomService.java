package com.hotel.services;

import com.hotel.models.Room;
import com.hotel.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomService {

    // Método para guardar una habitación en la base de datos
    public void saveRoom(Room room) {
        String query = "INSERT INTO rooms (type, price, available) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, room.getType());
            stmt.setDouble(2, room.getPrice());
            stmt.setBoolean(3, room.isAvailable());
            stmt.executeUpdate();
            System.out.println("Habitación guardada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al guardar la habitación: " + e.getMessage());
        }
    }

    // Método para obtener todas las habitaciones de la base de datos
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Room room = new Room(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getDouble("price"),
                    rs.getBoolean("available")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las habitaciones: " + e.getMessage());
        }
        return rooms;
    }

    // Método para actualizar una habitación en la base de datos
    public void updateRoom(Room room) {
        String query = "UPDATE rooms SET type = ?, price = ?, available = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, room.getType());
            stmt.setDouble(2, room.getPrice());
            stmt.setBoolean(3, room.isAvailable());
            stmt.setInt(4, room.getId());
            stmt.executeUpdate();
            System.out.println("Habitación actualizada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar la habitación: " + e.getMessage());
        }
    }

    // Método para eliminar una habitación de la base de datos
    public void deleteRoom(int id) {
        String query = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Habitación eliminada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar la habitación: " + e.getMessage());
        }
    }

    // Método para buscar habitaciones por tipo
    public List<Room> findRoomsByType(String type) {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE type = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Room room = new Room(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getDouble("price"),
                    rs.getBoolean("available")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar habitaciones por tipo: " + e.getMessage());
        }
        return rooms;
    }
}