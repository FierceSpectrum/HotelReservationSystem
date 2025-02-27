package com.hotel.services;

import com.hotel.models.Client;
import com.hotel.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientService {

    // Método para guardar un cliente en la base de datos
    public void saveClient(Client client) {
        String query = "INSERT INTO clients (name, email, phone) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getPhone());
            stmt.executeUpdate();
            System.out.println("Cliente guardado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al guardar el cliente: " + e.getMessage());
        }
    }

    // Método para obtener todos los clientes de la base de datos
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM clients";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Client client = new Client(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los clientes: " + e.getMessage());
        }
        return clients;
    }

    // Método para buscar un cliente por ID
    public Client findClientById(int id) {
        String query = "SELECT * FROM clients WHERE id = ?";
        Client client = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    client = new Client(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el cliente: " + e.getMessage());
        }
        return client;
    }
}