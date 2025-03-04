package com.hotel.services;

import com.hotel.models.Client;
import com.hotel.models.Reservation;
import com.hotel.models.Room;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

class InvoiceServiceTest {

    @Mock
    private ReservationService reservationService;
    
    @Mock
    private ClientService clientService;
    
    @Mock
    private RoomService roomService;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private InvoiceService invoiceService;

    @BeforeMethod
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Asegurar que el servicio usa el mock de DatabaseConnection
        invoiceService = new InvoiceService(reservationService, clientService, roomService);
    }

    @Test
    public void testCreateInvoice() throws SQLException {
        // Arrange
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), "Active");
        Client client = new Client(1, "John Doe", "john@example.com", "123456789");
        Room room = new Room(1, "Single", 100.0, true);

        // Configurar el mock para executeQuery
        when(reservationService.getReservation(1)).thenReturn(reservation);
        when(clientService.getClient(1)).thenReturn(client);
        when(roomService.getRoom(1)).thenReturn(room);

        // Generar la factura
        invoiceService.createInvoice(1);

        // Verificar que se llamó a los métodos correspondientes
        verify(reservationService, times(1)).getReservation(1);
        verify(clientService, times(1)).getClient(1);
        verify(roomService, times(1)).getRoom(1);
    }
}