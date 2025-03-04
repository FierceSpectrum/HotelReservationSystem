package com.hotel.services;

import com.hotel.models.Client;
import com.hotel.utils.DatabaseConnection;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * 
 */
public class ClientServiceTest {

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

    @BeforeMethod
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Ensure the service uses the mock of DatabaseConnection
        clientService = new ClientService(databaseConnection);

        // Configure the behavior of the mocks
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(connection.prepareStatement(any(), anyInt())).thenReturn(preparedStatement);
    }

    @Test
    public void testRegisterClient_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
        Client client = new Client(0, "John Doe", "john@example.com", "123456789");

        // Act
        clientService.registerClient(client);

        // Assert
        assertEquals(client.getId(), 1);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testRegisterClient_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));
        Client client = new Client(0, "John Doe", "john@example.com", "123456789");

        // Act
        clientService.registerClient(client);

        // Assert
        assertEquals(client.getId(), 0);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testGetAllClients_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getString("phone")).thenReturn("123456789");

        // Act
        List<Client> clients = clientService.getAllClients();

        // Assert
        assertNotNull(clients, "The client list should not be null.");
        assertEquals(clients.size(), 1);
        assertEquals(clients.get(0).getName(), "John Doe");
        assertEquals(clients.get(0).getEmail(), "john@example.com");
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetAllClients_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenThrow(new SQLException("Database error"));

        // Act
        List<Client> clients = clientService.getAllClients();

        // Assert
        assertNotNull(clients);
        assertTrue(clients.isEmpty());
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetClient_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getString("phone")).thenReturn("123456789");

        // Act
        Client foundClient = clientService.getClient(1);

        // Assert
        assertNotNull(foundClient);
        assertEquals(foundClient.getId(), 1);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetClient_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // Act
        Client foundClient = clientService.getClient(1);

        // Assert
        assertNull(foundClient);
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testUpdateClient_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenReturn(1);
        Client client = new Client(1, "John Doe", "john@example.com", "123456789");

        // Act
        clientService.updateClient(client);

        // Assert
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateClient_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));
        Client client = new Client(1, "John Doe", "john@example.com", "123456789");

        // Act
        clientService.updateClient(client);

        // Assert
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteClient_Success() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        clientService.deleteClient(1);

        // Assert
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteClient_Failure() throws SQLException {
        // Arrange
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        // Act
        clientService.deleteClient(1);

        // Assert
        verify(preparedStatement, times(1)).executeUpdate();
    }
}