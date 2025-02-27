package com.hotel.services;

import com.hotel.models.Reservation;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ReportService {
    private static final Logger LOGGER = Logger.getLogger(ReportService.class.getName());

    private final int totalRooms; // Total de habitaciones en el hotel

    public ReportService(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    // Generates a report of the hotel's occupancy
    public void generateOccupancyReport(List<Reservation> reservations) {
        LocalDate today = LocalDate.now();

        // Filtrar reservas activas para hoy
        List<Reservation> activeReservations = reservations.stream()
                .filter(r -> !r.getCheckOutDate().isBefore(today) && !r.getCheckInDate().isAfter(today))
                .collect(Collectors.toList());

        int occupiedRooms = activeReservations.size();
        double occupancyRate = (double) occupiedRooms / totalRooms * 100;

        LOGGER.info("==== Hotel Occupancy Report ====");
        LOGGER.info("Total Reservations: " + reservations.size());
        LOGGER.info("Occupied Rooms Today: " + occupiedRooms + " / " + totalRooms);
        LOGGER.info("Occupancy Rate: " + String.format("%.2f", occupancyRate) + "%");
    }
}
