package com.hotel.services;

import com.hotel.models.Client;
import com.hotel.utils.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class ClientService {

    // Registra un nuevo cliente en el sistema
    public void registerClient(Client client) throws SQLException {
        String sql = "INSERT INTO clients (id, name, email, phone) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, client.getId());
            stmt.setString(2, client.getName());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhone());

            stmt.executeUpdate();
        }
    }

    // Obtiene un cliente por su ID
    public Client getClient(int clientId) throws SQLException {
        String sql = "SELECT * FROM clients WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"));
                }
            }
        }
        return null;
    }

    // Actualiza atributos dinámicos de un cliente
    public boolean updateClientAttributes(int clientId, Map<String, Object> attributes) throws SQLException {
        if (attributes == null || attributes.isEmpty()) {
            return false;
        }

        StringBuilder sqlBuilder = new StringBuilder("UPDATE clients SET ");
        List<Object> params = new ArrayList<>();

        for (Iterator<Map.Entry<String, Object>> it = attributes.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Object> entry = it.next();
            String attributeName = entry.getKey();

            if (!isValidAttribute(attributeName)) {
                continue;
            }

            sqlBuilder.append(attributeName).append(" = ?");
            params.add(entry.getValue());

            if (it.hasNext()) {
                sqlBuilder.append(", ");
            }
        }

        if (params.isEmpty()) {
            return false;
        }

        sqlBuilder.append(" WHERE id = ?");
        params.add(clientId);

        String sql = sqlBuilder.toString();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    stmt.setString(i + 1, (String) param);
                } else if (param instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) param);
                }
            }

            return stmt.executeUpdate() > 0;
        }
    }

    // Elimina un cliente por su ID
    public boolean deleteClient(int clientId) throws SQLException {
        String sql = "DELETE FROM clients WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);

            return stmt.executeUpdate() > 0;
        }
    }

    // Valida que el atributo sea válido para actualización
    private boolean isValidAttribute(String attributeName) {
        return Arrays.asList("name", "email", "phone").contains(attributeName);
    }
}
