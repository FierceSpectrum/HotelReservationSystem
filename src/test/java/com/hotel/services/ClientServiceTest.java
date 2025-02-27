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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Asegurar que el servicio usa el mock de DatabaseConnection
        clientService = new ClientService(databaseConnection);
    
        // Configurar el comportamiento de los mocks para ambos tipos de prepareStatement
        when(databaseConnection.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(connection.prepareStatement(any(), anyInt())).thenReturn(preparedStatement);

    }

    @Test
    void testSaveClient() throws SQLException {
        // Configurar el mock para executeUpdate y getGeneratedKeys
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
    
        // Crear un cliente con un email único
        Client client = new Client(0, "John Doe", "john@example.com", "123456789");
    
        // Guardar el cliente
        clientService.saveClient(client);
    
        // Verificar que se asignó el ID
        assertEquals(1, client.getId());
    
        // Verificar que se llamó a executeUpdate
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testFindClientById() throws SQLException {
        // Configurar el mock para executeQuery
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); // Simular que hay un resultado
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getString("phone")).thenReturn("123456789");
    
        // Buscar el cliente por ID
        Client foundClient = clientService.findClientById(1);
    
        // Verificar que el cliente se encontró correctamente
        assertNotNull(foundClient, "El cliente no debe ser nulo.");
        assertEquals(1, foundClient.getId());
        assertEquals("John Doe", foundClient.getName());
    
        // Verificar que se llamó a executeQuery
        verify(preparedStatement, times(1)).executeQuery();
    }
}