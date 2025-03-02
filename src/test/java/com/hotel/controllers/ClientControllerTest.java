package com.hotel.controllers;

import com.hotel.models.Client;
import com.hotel.services.ClientService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        clientController = new ClientController(clientService);
    }

    @Test
    public void testRegisterClient() {
        // Arrange
        Client client = new Client(1, "John Doe", "john@example.com", "123456789");
        doNothing().when(clientService).saveClient(client);

        // Act
        clientController.registerClient("John Doe", "john@example.com", "123456789");

        // Assert
        verify(clientService, times(1)).saveClient(any(Client.class));
    }

    @Test
    public void testFindClientById() {
        // Arrange
        Client client = new Client(1, "John Doe", "john@example.com", "123456789");
        when(clientService.findClientById(1)).thenReturn(client);

        // Act
        Client foundClient = clientController.findClientById(1);

        // Assert
        assertNotNull(foundClient);
        assertEquals(foundClient.getName(), "John Doe");
    }

    @Test
    public void testGetAllClients() {
        // Arrange
        List<Client> clients = Arrays.asList(
                new Client(1, "John Doe", "john@example.com", "123456789"),
                new Client(2, "Jane Doe", "jane@example.com", "987654321"));
        when(clientService.getAllClients()).thenReturn(clients); // FIX

        // Act
        List<Client> result = clientController.getAllClients();

        // Assert
        assertEquals(result.size(), 2);
    }

}