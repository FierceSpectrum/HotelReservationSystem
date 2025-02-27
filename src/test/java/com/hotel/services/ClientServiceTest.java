package com.hotel.services;

import com.hotel.models.Client;
import com.hotel.utils.DatabaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    void testSaveClient_Success() throws SQLException {
        Client client = new Client(1, "John Doe", "john@example.com", "123456789");
        when(preparedStatement.executeUpdate()).thenReturn(1); // Simular una inserción exitosa

        clientService.saveClient(client);

        verify(preparedStatement, times(1)).executeUpdate();
        System.out.println("Prueba de guardar cliente exitosa.");
    }

    @Test
    void testSaveClient_Failure() throws SQLException {
        Client client = new Client(1, "John Doe", "john@example.com", "123456789");
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Error de base de datos"));

        assertThrows(SQLException.class, () -> clientService.saveClient(client));
        System.out.println("Prueba de guardar cliente fallida.");
    }

    @Test
    void testGetAllClients_Success() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false); // Simular un resultado
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getString("phone")).thenReturn("123456789");

        List<Client> clients = clientService.getAllClients();
        assertFalse(clients.isEmpty());
        assertEquals(1, clients.size());
        System.out.println("Prueba de obtener todos los clientes exitosa.");
    }

    @Test
    void testGetAllClients_Failure() throws SQLException {
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("Error de base de datos"));

        assertThrows(SQLException.class, () -> clientService.getAllClients());
        System.out.println("Prueba de obtener todos los clientes fallida.");
    }

    @Test
    void testFindClientById_Success() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); // Simular un resultado
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getString("phone")).thenReturn("123456789");

        Client client = clientService.findClientById(1);
        assertNotNull(client);
        assertEquals("John Doe", client.getName());
        System.out.println("Prueba de buscar cliente por ID exitosa.");
    }

    @Test
    void testFindClientById_Failure() throws SQLException {
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("Error de base de datos"));

        assertThrows(SQLException.class, () -> clientService.findClientById(1));
        System.out.println("Prueba de buscar cliente por ID fallida.");
    }
}