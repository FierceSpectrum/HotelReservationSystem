package com.hotel.services;

import com.hotel.models.Reservation;
import java.time.LocalDate;
import java.util.List;

public class NotificationService {
    // Sends notifications to clients for upcoming check-ins
    public void sendNotifications(List<Reservation> reservations) {
        reservations.stream()
                .filter(r -> r.getCheckInDate().minusDays(1).equals(LocalDate.now()))
                .forEach(r -> System.out
                        .println("Notification sent to Client ID " + r.getClientId() + " for check-in tomorrow."));
    }
}
