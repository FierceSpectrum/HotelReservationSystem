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

    private final DatabaseConnection databaseConnection;

    public RoomService(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // Registra una nueva habitación en el sistema
    public void registerRoom(Room room) {
        String sql = "INSERT INTO rooms (type, price, available) VALUES (?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, room.getType());
            stmt.setDouble(2, room.getPrice());
            stmt.setBoolean(3, room.isAvailable());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    room.setId(id);
                    System.out.println("Habitación guardada correctamente con ID: " + id);
                } else {
                    throw new SQLException("No se pudo obtener el ID generado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar la habitación: " + e.getMessage());
        }
    }

    // Busca habitaciones según tipo, precio y disponibilidad
    public List<Room> searchRooms(String type, Double price, Boolean available) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM rooms WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (type != null) {
            sqlBuilder.append(" AND type = ?");
            params.add(type);
        }

        if (price != null) {
            sqlBuilder.append(" AND price <= ?");
            params.add(price);
        }

        if (available != null) {
            sqlBuilder.append(" AND available = ?");
            params.add(available);
        }

        String sql = sqlBuilder.toString();
        List<Room> results = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Establecer los parámetros dinámicos
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    stmt.setString(i + 1, (String) param);
                } else if (param instanceof Double) {
                    stmt.setDouble(i + 1, (Double) param);
                } else if (param instanceof Boolean) {
                    stmt.setBoolean(i + 1, (Boolean) param);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room(
                            rs.getInt("id"),
                            rs.getString("type"),
                            rs.getDouble("price"),
                            rs.getBoolean("available"));
                    results.add(room);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las habitaciones: " + e.getMessage());
        }

        return results;
    }


    // Actualiza una habitación
    public void updateRoom(Room room) {
        String sql = "UPDATE rooms SET type = ?, price = ?, available = ? WHERE id = ?";
        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    // Actualiza el estado de disponibilidad de una habitación
    public void updateAvailability(int roomId, boolean available) {
        String sql = "UPDATE rooms SET available = ? WHERE id = ?";
        
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, available);
            stmt.setInt(2, roomId);
            
            stmt.executeUpdate();
            System.out.println("Disponibilidad de la habitación actualizada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar la disponibilidad de la habitación: " + e.getMessage());
        }
    }

    // Elimina una habitación por su ID
    public void deleteRoom(int roomId) {
        String sql = "DELETE FROM rooms WHERE id = ?";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);

            stmt.executeUpdate();
            System.out.println("Habitación eliminada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al eliminar la habitación: " + e.getMessage());
        }
    }
}