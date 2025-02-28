package com.hotel.controllers;

import com.hotel.services.InvoiceService;

public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // Generar una factura
    public void generateInvoice(int reservationId) {
        invoiceService.generateInvoice(reservationId);
        System.out.println("Factura generada correctamente");
    }
}
