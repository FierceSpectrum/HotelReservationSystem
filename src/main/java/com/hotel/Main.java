package com.hotel;

import com.hotel.models.Client;
import com.hotel.models.Room;
import com.hotel.models.Reservation;
import com.hotel.services.ClientService;
import com.hotel.services.RoomService;
import com.hotel.utils.DatabaseConnection;
import com.hotel.services.ReservationService;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        // Crear instancias de los servicios
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        ClientService clientService = new ClientService(databaseConnection);
        RoomService roomService = new RoomService();
        ReservationService reservationService = new ReservationService();

        try {
            // Prueba de ClientService
            System.out.println("=== Prueba de ClientService ===");
            Client client = new Client(0, "John Doe", "john4@example.com", "123456789");
            clientService.saveClient(client);
            // System.out.println("Cliente guardado correctamente.");

            Client foundClient = clientService.findClientById(5);
            System.out.println("Cliente encontrado: " + foundClient.getName());

            // Prueba de RoomService
            System.out.println("\n=== Prueba de RoomService ===");
            Room room = new Room(0, "Double", 250.0, true);
            roomService.saveRoom(room);
            System.out.println("Habitación guardada correctamente.");

            // Prueba de ReservationService
            System.out.println("\n=== Prueba de ReservationService ===");
            Reservation reservation = new Reservation(0, 1, 1, LocalDate.now(), LocalDate.now().plusDays(2), "Active");
            reservationService.createReservation(reservation);
            System.out.println("Reserva creada correctamente.");

            // Mostrar todas las reservas
            System.out.println("\n=== Historial de Reservas ===");
            reservationService.getReservationHistory(1).forEach(res -> {
                System.out.println("Reserva ID: " + res.getId() + ", Habitación ID: " + res.getRoomId());
            });

        } catch (Exception e) {
            System.err.println("Error durante las pruebas: " + e.getMessage());
        }
    }
}