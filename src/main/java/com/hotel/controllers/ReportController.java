package com.hotel.controllers;

import com.hotel.services.ReportService;
import java.time.LocalDate;

public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Generar un reporte de ocupación
    public void generateOccupancyReport(LocalDate startDate, LocalDate endDate) {
        reportService.generateReport(startDate, endDate);
    }
}
