package com.hotel;

import com.hotel.controllers.ClientController;
import com.hotel.models.Client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Scanner;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MainTest {

    @Mock
    private ClientController clientController;

    private InputStream originalSystemIn;

    private Field scannerField;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        originalSystemIn = System.in;

        // Usar reflection para accedr al campo estático scanner de Main
        scannerField = Main.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        System.setIn(originalSystemIn);

        scannerField.setAccessible(true);
        scannerField.set(null, new Scanner(System.in));
    }

    @Test
    public void testLogin_AdminUser() throws Exception {
        // Arrange
        String adminEmail = "admin1@admin.com\n";  // Agregar \n para simular ENTER
        System.setIn(new ByteArrayInputStream(adminEmail.getBytes()));

        // Reemplaza el Scanner estático en Main
        scannerField.set(null, new Scanner(System.in));

        // Act
        String role = Main.login(clientController);

        // Assert
        Assert.assertEquals(role, "ADMIN");
    }

    @Test
    public void testLogin_ClientUser() throws Exception {
        // Arrange
        String clientEmail = "client@example.com\n";
        System.setIn(new ByteArrayInputStream(clientEmail.getBytes()));

        // Remplaza el Scanner estático en Main
        scannerField.set(null, new Scanner(System.in));

        // Configurar el mock de ClientController
        Client mockClient = new Client(1, "Test Client", clientEmail.trim(), "123456789");
        Mockito.when(clientController.findClientByEmail(clientEmail.trim())).thenReturn(mockClient);

        // Act
        String role = Main.login(clientController);

        // Assert
        Assert.assertEquals(role, "CLIENT");
    }

    @Test
    public void testLogin_UnknownUser() throws Exception {
        // Arrange
        String unknownEmail = "unknown@example.com\n";
        System.setIn(new ByteArrayInputStream(unknownEmail.getBytes()));

        // Remplaza el Scanner estático en Main
        scannerField.set(null, new Scanner(System.in));

        // Configurar el mock de ClientController
        Mockito.when(clientController.findClientByEmail(unknownEmail.trim())).thenReturn(null);

        // Act
        String role = Main.login(clientController);

        // Assert
        Assert.assertEquals(role, "UNKNOWN");
    }
}
