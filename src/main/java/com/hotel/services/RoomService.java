package com.hotel.services;

import com.hotel.models.Room;
import com.hotel.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomService {
    
    // Registra una nueva habitación en el sistema
    public void registerRoom(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (id, type, price, available) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, room.getId());
            stmt.setString(2, room.getType());
            stmt.setDouble(3, room.getPrice());
            stmt.setBoolean(4, room.isAvailable());
            
            stmt.executeUpdate();
        }
    }
    
    // Busca habitaciones según tipo, precio y disponibilidad
    public List<Room> searchRooms(String type, Double price, Boolean available) throws SQLException {
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
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
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
                        rs.getBoolean("available")
                    );
                    results.add(room);
                }
            }
        }
        
        return results;
    }
    
    // Verifica si una habitación específica está disponible
    public boolean checkAvailability(int roomId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rooms WHERE id = ? AND available = true";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    // Actualiza el estado de disponibilidad de una habitación
    public void updateAvailability(int roomId, boolean available) throws SQLException {
        String sql = "UPDATE rooms SET available = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, available);
            stmt.setInt(2, roomId);
            
            stmt.executeUpdate();
        }
    }

    // Elimina una habitación por su ID
    public boolean deleteRoom(int roomId) throws SQLException {
        String sql = "DELETE FROM rooms WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    // Actualiza los atributos de una habitación
    public boolean updateRoomAttributes(int roomId, Map<String, Object> attributes) throws SQLException {
        if (attributes == null || attributes.isEmpty()) {
            return false;
        }
        
        StringBuilder sqlBuilder = new StringBuilder("UPDATE rooms SET ");
        List<Object> params = new ArrayList<>();
        
        int count = 0;
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String attributeName = entry.getKey();
            
            // Validar que solo se actualicen atributos válidos
            if (!isValidAttribute(attributeName)) {
                continue;
            }
            
            if (count > 0) {
                sqlBuilder.append(", ");
            }
            
            sqlBuilder.append(attributeName).append(" = ?");
            params.add(entry.getValue());
            count++;
        }
        
        // Si no hay atributos válidos para actualizar, retornar
        if (count == 0) {
            return false;
        }
        
        sqlBuilder.append(" WHERE id = ?");
        params.add(roomId);
        
        String sql = sqlBuilder.toString();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Establecer los parámetros dinámicos
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    stmt.setString(i + 1, (String) param);
                } else if (param instanceof Double || param instanceof Integer) {
                    if (param instanceof Integer) {
                        stmt.setInt(i + 1, (Integer) param);
                    } else {
                        stmt.setDouble(i + 1, (Double) param);
                    }
                } else if (param instanceof Boolean) {
                    stmt.setBoolean(i + 1, (Boolean) param);
                }
            }
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    // Método auxiliar para validar nombres de atributos
    private boolean isValidAttribute(String attributeName) {
        return attributeName.equals("type") || 
               attributeName.equals("price") || 
               attributeName.equals("available");
    }
}