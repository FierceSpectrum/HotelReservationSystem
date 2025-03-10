package com.hotel.controllers;

import com.hotel.services.NotificationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationController = new NotificationController(notificationService);
    }

    @Test
    public void testSendCheckInNotifications() {
        // Arrange
        doNothing().when(notificationService).createNotifications(2);

        // Act
        notificationController.sendCheckInNotifications(2);

        // Assert
        verify(notificationService, times(1)).createNotifications(2);
    }
}