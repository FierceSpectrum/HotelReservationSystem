package com.hotel.controllers;

import com.hotel.services.ReportService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateOccupancyReport() {
        // Arrange
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);
        doNothing().when(reportService).generateOccupancyReport(startDate, endDate);

        // Act
        reportController.generateOccupancyReport(startDate, endDate);

        // Assert
        verify(reportService, times(1)).generateOccupancyReport(startDate, endDate);
    }
}