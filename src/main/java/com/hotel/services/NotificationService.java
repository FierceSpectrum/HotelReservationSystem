package com.hotel.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.hotel.models.Reservation;

public class NotificationService {
    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());

    // Sends notifications to clients for upcoming check-ins
    public void sendNotifications(List<Reservation> reservations) {
        LocalDate today = LocalDate.now();

        reservations.parallelStream()
                .filter(r -> Optional.ofNullable(r.getCheckInDate())
                        .map(date -> date.minusDays(1).equals(today))
                        .orElse(false))
                .forEach(this::sendNotification);
    }

    private void sendNotification(Reservation reservation) {
        String message = "Notification sent to Client ID " + reservation.getClientId() + " for check-in tomorrow.";
        LOGGER.info(message);
    }
}
