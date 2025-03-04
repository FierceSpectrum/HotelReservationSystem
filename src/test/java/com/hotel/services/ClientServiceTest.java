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

        // Asegurar que el servicio usa el mock de DatabaseConnection
        clientService = new ClientService(databaseConnection);

        // Configurar el comportamiento de los mocks
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(connection.prepareStatement(any(), anyInt())).thenReturn(preparedStatement);
    }

    @Test
    public void testRegisterClient() throws SQLException {
        // Configurar el mock para executeUpdate y getGeneratedKeys
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        // Crear un cliente con un email único
        Client client = new Client(0, "John Doe", "john@example.com", "123456789");

        // Guardar el cliente
        clientService.registerClient(client);

        // Verificar que se asignó el ID
        assertEquals(client.getId(), 1);

        // Verificar que se llamó a executeUpdate
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testGetAllClients() throws SQLException {
        // Configurar el mock para executeQuery
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getString("phone")).thenReturn("123456789");

        // Obtener todos los clientes
        List<Client> clients = clientService.getAllClients();

        // Verificar que se obtuvieron los clientes correctamente
        assertNotNull(clients, "La lista de clientes no debe ser nula.");
        assertEquals(clients.size(), 1);
        assertEquals(clients.get(0).getName(), "John Doe");
        assertEquals(clients.get(0).getEmail(), "john@example.com");

        // Verificar que se llamó a executeQuery
        verify(preparedStatement, times(1)).executeQuery();

    }

    @Test
    public void testGetClient() throws SQLException {
        // Configurar el mock para executeQuery
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); // Simular que hay un resultado
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getString("phone")).thenReturn("123456789");

        // Buscar el cliente por ID
        Client foundClient = clientService.getClient(1);

        // Verificar que el cliente se encontró correctamente
        assertNotNull(foundClient, "El cliente no debe ser nulo.");
        assertEquals(foundClient.getId(), 1);
        assertEquals(foundClient.getName(), "John Doe");
        assertEquals(foundClient.getEmail(), "john@example.com");

        // Verificar que se llamó a executeQuery
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testUpdateClient() throws SQLException {
        // Configurar el mock para executeUpdate
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Crear cliente
        Client client = new Client(1, "John Doe", "john@example.com", "123456789");

        // Actualizar el cliente
        clientService.updateClient(client);

        // Verificar que se llamó a executeQuery
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteClient() throws SQLException {
        // Configurar el mock para executeUpdate
        when(preparedStatement.executeUpdate()).thenReturn(1);


        clientService.deleteClient(1);

        // Verificar que se llamó a executeUpdate
        verify(preparedStatement, times(1)).executeUpdate();
    }
}