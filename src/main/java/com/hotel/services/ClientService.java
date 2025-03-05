package com.hotel.services;

import com.hotel.models.Client;
import com.hotel.utils.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class ClientService {

    private final DatabaseConnection databaseConnection;

    // Constructor que recibe una instancia de DatabaseConnection
    public ClientService(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // Registra un nuevo cliente en el sistema
    public void registerClient(Client client) {
        String sql = "INSERT INTO clients (name, email, phone) VALUES (?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getPhone());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    client.setId(id);
                    System.out.println("Cliente guardado correctamente con ID: " + id);
                } else {
                    throw new SQLException("No se pudo obtener el ID generado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar el cliente: " + e.getMessage());
        }
    }

    // Obtiene todos los clientes de la base de datos
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"));
                clients.add(client);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los clientes: " + e.getMessage());
        }
        System.out.println("Clientes obtenidos correctamente.");
        return clients;
    }

    // Obtiene un cliente por su ID
    public Client getClient(int clientId) {
        String sql = "SELECT * FROM clients WHERE id = ?";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Cliente encontrado correctamente.");
                    return new Client(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar un cliente: " + e.getMessage());
        }
        return null;
    }

    // Actualiza atributos de un cliente
    public void updateClient(Client client) {
        String sql = "UPDATE clients SET name = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getPhone());
            stmt.setInt(4, client.getId());
            stmt.executeUpdate();
            System.out.println("Cliente actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar el cliente: " + e.getMessage());
        }
    }

    // Elimina un cliente por su ID
    public void deleteClient(int clientId) {
        String sql = "DELETE FROM clients WHERE id = ?";

        try (Connection conn = databaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            stmt.executeUpdate();

            System.out.println("Cliente eliminado correctamente");
        } catch (SQLException e) {
            System.err.println("Error al eliminar un cliente: " + e.getMessage());
        }
    }

    // Obtiene un cliente por su email
    public Client getClientByEmail(String email) {
        List<Client> clients = getAllClients();
        for (Client client : clients) {
            if (client.getEmail().trim().equalsIgnoreCase(email)) {
                System.out.println("Cliente encontrado correctamente.");
                return client;
            }
        }
        System.err.println("Cliente no encontrado con el email: " + email);
        return null;
    }
}
