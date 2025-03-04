package com.hotel.controllers;

import com.hotel.services.NotificationService;

public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Enviar notificación de check-in
    public void sendCheckInNotifications(int daysBefore) {
        notificationService.createNotifications(daysBefore);
    }
}
