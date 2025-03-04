package com.hotel.controllers;

import com.hotel.services.InvoiceService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class InvoiceControllerTest {

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        invoiceController = new InvoiceController(invoiceService);
    }

    @Test
    public void testGenerateInvoice() {
        // Arrange
        doNothing().when(invoiceService).createInvoice(1);

        // Act
        invoiceController.generateInvoice(1);

        // Assert
        verify(invoiceService, times(1)).createInvoice(1);
    }
}