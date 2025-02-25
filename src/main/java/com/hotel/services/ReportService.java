package com.hotel.services;

import com.hotel.models.Reservation;
import java.util.List;

public class ReportService {
    // Generates a report of the hotel's occupancy
    public void generateOccupancyReport(List<Reservation> reservations) {
        System.out.println("Occupancy Report:");
        System.out.println("Total Reservations: " + reservations.size());
    }
}
