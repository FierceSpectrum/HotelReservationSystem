package com.hotel;

import com.hotel.models.Client;
import com.hotel.models.Room;
import com.hotel.models.Reservation;
import com.hotel.services.ClientService;
import com.hotel.services.RoomService;
import com.hotel.services.ReservationService;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Obtener la instancia de DatabaseConnection
        // DatabaseConnection db = DatabaseConnection.getInstance();

        // Crear instancias de los servicios
        ClientService clientService = new ClientService();
        RoomService roomService = new RoomService();
        ReservationService reservationService = new ReservationService();

        try {
            // Prueba de ClientService
            System.out.println("=== Prueba de ClientService ===");
            Client client = new Client(2, "John Doe", "john2@example.com", "123456789");
            clientService.saveClient(client);
            // System.out.println("Cliente guardado correctamente.");

            Client foundClient = clientService.findClientById(1);
            System.out.println("Cliente encontrado: " + foundClient.getName());

            // Prueba de RoomService
            System.out.println("\n=== Prueba de RoomService ===");
            Room room = new Room(1, "Single", 100.0, true);
            roomService.saveRoom(room);
            System.out.println("Habitación guardada correctamente.");

            // Prueba de ReservationService
            System.out.println("\n=== Prueba de ReservationService ===");
            Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), "Active");
            reservationService.createReservation(reservation);
            System.out.println("Reserva creada correctamente.");

            // Mostrar todas las reservas
            System.out.println("\n=== Historial de Reservas ===");
            reservationService.getReservationHistory(1).forEach(res -> {
                System.out.println("Reserva ID: " + res.getId() + ", Habitación ID: " + res.getRoomId());
            });

        } catch (Exception e) {
            System.err.println("Error durante las pruebas: " + e.getMessage());
        } finally {
            // Cerrar la conexión a la base de datos
            // db.closeConnection();
        }
    }
}