/**
 * Main class for the Hotel Reservation System.
 * This class initializes the database connection, services, and controllers,
 * and provides the main menu for both admin and client roles.
 * 
 * The main method simulates a login process and displays the appropriate menu
 * based on the user's role.
 * 
 * The admin menu allows the admin to:
 * - Register a room
 * - Register a client
 * - Generate an invoice
 * - Send check-in notifications
 * - Generate an occupancy report
 * 
 * The client menu allows the client to:
 * - Search for rooms
 * - Create a reservation
 * - Cancel a reservation
 * - View reservation history
 * - Check room availability
 * 
 * The class uses a Scanner object for user input and maintains a list of admin
 * emails for role verification.
 * 
 * Methods:
 * - main(String[] args): Entry point of the application.
 * - login(ClientController clientController): Simulates the login process and
 *   returns the user's role.
 * - showAdminMenu(ClientController clientController, RoomController roomController,
 *   InvoiceController invoiceController, NotificationController notificationController,
 *   ReportController reportController): Displays the admin menu and handles admin
 *   actions.
 * - showClientMenu(RoomController roomController, ReservationController reservationController,
 *   ClientController clientController): Displays the client menu and handles client
 *   actions.
 * - registerRoom(RoomController roomController): Registers a new room.
 * - registerClient(ClientController clientController): Registers a new client.
 * - generateInvoice(InvoiceController invoiceController): Generates an invoice for a
 *   reservation.
 * - sendCheckInNotifications(NotificationController notificationController): Sends
 *   check-in notifications.
 * - generateOccupancyReport(ReportController reportController): Generates an occupancy
 *   report.
 * - searchRooms(RoomController roomController): Searches for rooms based on criteria.
 * - createReservation(ReservationController reservationController): Creates a new
 *   reservation.
 * - cancelReservation(ReservationController reservationController): Cancels a reservation.
 * - viewReservationHistory(ReservationController reservationController): Displays the
 *   client's reservation history.
 * - checkAvailability(RoomController roomController): Checks room availability.
 */
package com.hotel;

import com.hotel.controllers.*;
import com.hotel.models.*;
import com.hotel.services.*;
import com.hotel.utils.DatabaseConnection;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static List<String> adminEmails = List.of("admin1@admin.com", "admin2@admin.com", "admin3@admin.com");
    private static int clientId;

    /**
     * Main method to start the application.
     * Initializes the database connection, services, and controllers.
     * Simulates login and displays the appropriate menu based on the user role.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Inicializar la conexión a la base de datos
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();

        // Inicializar los servicios
        ClientService clientService = new ClientService(dbConnection);
        RoomService roomService = new RoomService(dbConnection);
        ReservationService reservationService = new ReservationService(dbConnection, roomService);
        InvoiceService invoiceService = new InvoiceService(reservationService, clientService, roomService);
        NotificationService notificationService = new NotificationService(dbConnection);
        ReportService reportService = new ReportService(dbConnection);

        // Inicializar los controladores
        ClientController clientController = new ClientController(clientService);
        RoomController roomController = new RoomController(roomService);
        ReservationController reservationController = new ReservationController(reservationService);
        InvoiceController invoiceController = new InvoiceController(invoiceService);
        NotificationController notificationController = new NotificationController(notificationService);
        ReportController reportController = new ReportController(reportService);

        // Simular login
        String role = login(clientController);
        if (role.equals("ADMIN")) {
            showAdminMenu(clientController, roomController, invoiceController, notificationController,
                    reportController);
        } else if (role.equals("CLIENT")) {
            showClientMenu(roomController, reservationController, clientController);
        } else {
            System.out.println("Rol no válido");
        }
    }

    /**
     * Simulates the login process.
     * Verifies if the email belongs to an admin or a client.
     *
     * @param clientController The client controller
     * @return The role of the user ("ADMIN", "CLIENT", or "UNKNOWN")
     */
    public static String login(ClientController clientController) {
        System.out.println("Bienvenido al sistema de gestión de hotel");
        System.out.println("Por favor, ingrese su correo electrónico:");
        String email = scanner.nextLine();

        // Verificar si el correo pertenece a un administrador
        if (adminEmails.contains(email)) {
            return "ADMIN";
        }

        // Verificar si el correo pertenece a un cliente
        Client client = clientController.findClientByEmail(email);
        if (client != null) {
            clientId = client.getId();
            return "CLIENT";
        }

        return "UNKNOWN";
    }

    /**
     * Displays the admin menu and handles admin operations.
     *
     * @param clientController The client controller
     * @param roomController The room controller
     * @param invoiceController The invoice controller
     * @param notificationController The notification controller
     * @param reportController The report controller
     */
    public static void showAdminMenu(ClientController clientController, RoomController roomController,
            InvoiceController invoiceController, NotificationController notificationController,
            ReportController reportController) {
        while (true) {
            String options = "\n--- Menú de administrador ---\n"
                    + "1. Registrar habitación\n"
                    + "2. Registrar cliente\n"
                    + "3. Generar factura\n"
                    + "4. Enviar notificación de check-in\n"
                    + "5. Generar reporte de ocupación\n"
                    + "6. Salir\n"
                    + "Seleccione una opción: ";
            System.out.print(options);
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    registerRoom(roomController);
                    break;
                case 2:
                    registerClient(clientController);
                    break;
                case 3:
                    generateInvoice(invoiceController);
                    break;
                case 4:
                    sendCheckInNotifications(notificationController);
                    break;
                case 5:
                    generateOccupancyReport(reportController);
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción inválida");

            }
        }
    }

    /**
     * Displays the client menu and handles client operations.
     *
     * @param roomController The room controller
     * @param reservationController The reservation controller
     * @param clientController The client controller
     */
    public static void showClientMenu(RoomController roomController, ReservationController reservationController,
            ClientController clientController) {
        while (true) {
            String options = "\n--- Menú de cliente ---\n"
                    + "1. Buscar habitaciones\n"
                    + "2. Reservar habitación\n"
                    + "3. Cancelar reservación\n"
                    + "4. Ver historial de reservaciones\n"
                    + "5. Consultar disponibilidad\n"
                    + "6. Salir\n"
                    + "Seleccione una opción: ";
            System.out.print(options);
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    searchRooms(roomController);
                    break;
                case 2:
                    createReservation(reservationController);
                    break;
                case 3:
                    cancelReservation(reservationController);
                    break;
                case 4:
                    viewReservationHistory(reservationController);
                    break;
                case 5:
                    checkAvailability(roomController);
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    // Methods for admin menu

    /**
     * Registers a new room.
     *
     * @param roomController The room controller
     */
    public static void registerRoom(RoomController roomController) {
        System.out.println("Ingrese el tipo de habitación:");
        String type = scanner.nextLine();
        System.out.println("Ingrese el precio: ");
        double price = scanner.nextDouble();
        System.out.println("¿Está disponible? (true/false):");
        boolean available = scanner.nextBoolean();
        scanner.nextLine();

        roomController.registerRoom(type, price, available);
    }

    /**
     * Registers a new client.
     *
     * @param clientController The client controller
     */
    public static void registerClient(ClientController clientController) {
        System.out.println("Ingrese el nombre del cliente:");
        String name = scanner.nextLine();
        System.out.println("Ingrese el email del cliente:");
        String email = scanner.nextLine();
        System.out.println("Ingrese el teléfono del cliente:");
        String phone = scanner.nextLine();

        clientController.registerClient(name, email, phone);
    }

    /**
     * Generates an invoice for a reservation.
     *
     * @param invoiceController The invoice controller
     */
    public static void generateInvoice(InvoiceController invoiceController) {
        System.out.println("Ingrese el ID de la reservación:");
        int reservationId = scanner.nextInt();
        scanner.nextLine();

        invoiceController.generateInvoice(reservationId);
    }

    /**
     * Sends check-in notifications.
     *
     * @param notificationController The notification controller
     */
    public static void sendCheckInNotifications(NotificationController notificationController) {
        System.out.println("Ingrese los días antes del check-in para enviar notificaciones: ");
        int daysBefore = scanner.nextInt();
        scanner.nextLine();

        notificationController.sendCheckInNotifications(daysBefore);
    }

    /**
     * Generates an occupancy report.
     *
     * @param reportController The report controller
     */
    public static void generateOccupancyReport(ReportController reportController) {
        System.out.println("Ingrese la fecha de inicio (yyyy-MM-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.println("Ingrese la fecha de fin (yyyy-MM-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        reportController.generateOccupancyReport(startDate, endDate);
    }

    // Methods for client menu

    /**
     * Searches for rooms based on criteria.
     *
     * @param roomController The room controller
     */
    public static void searchRooms(RoomController roomController) {
        System.out.println("Ingrese el tipo de habitación (opcional, presione Enter para omitir):");
        String type = scanner.nextLine();
        System.out.println("Ingrese el precio máximo (opcional, presione Enter para omitir):");
        Double priceInput = scanner.nextLine().isEmpty() ? null : Double.parseDouble(scanner.nextLine());
        System.out.println("¿Está disponible? (opcional, presione Enter para omitir):");
        Boolean available = scanner.nextLine().isEmpty() ? null : Boolean.parseBoolean(scanner.nextLine());
        List<Room> rooms = roomController.findRoomByType(type, priceInput, available);
        rooms.forEach(room -> System.out.println("Habitación ID: " + room.getId() + ", Tipo: " + room.getType()
                + ", Precio: " + room.getPrice() + ", Disponible: " + room.isAvailable()));
    }

    /**
     * Creates a new reservation.
     *
     * @param reservationController The reservation controller
     */
    public static void createReservation(ReservationController reservationController) {
        System.out.println("Ingrese el ID de la habitación:");
        int roomId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Ingrese la fecha de check-in (yyyy-MM-dd):");
        LocalDate checkInDate = LocalDate.parse(scanner.nextLine());
        System.out.println("Ingrese la fecha de check-out (yyyy-MM-dd):");
        LocalDate checkOutDate = LocalDate.parse(scanner.nextLine());
        String staus = "Active";

        reservationController.createReservation(clientId, roomId, checkInDate, checkOutDate, staus);
    }

    /**
     * Cancels an existing reservation.
     *
     * @param reservationController The reservation controller
     */
    public static void cancelReservation(ReservationController reservationController) {
        System.out.println("Ingrese el ID de la reservación:");
        int reservationId = scanner.nextInt();
        scanner.nextLine();

        reservationController.cancelReservation(reservationId);
    }

    /**
     * Views the reservation history of the client.
     *
     * @param reservationController The reservation controller
     */
    public static void viewReservationHistory(ReservationController reservationController) {
        scanner.nextLine();

        List<Reservation> reservations = reservationController.getClientReservations(clientId);
        reservations.forEach(reservation -> System.out.println("Reservación ID: " + reservation.getId()
                + ", Habitación ID: " + reservation.getRoomId()));
    }

    /**
     * Checks the availability of rooms.
     *
     * @param roomController The room controller
     */
    public static void checkAvailability(RoomController roomController) {
        List<Room> rooms = roomController.getAllRooms();
        rooms.forEach(room -> System.out.println("Habitación ID: " + room.getId() + ", Tipo: " + room.getType()
                + ", Precio: " + room.getPrice() + ", Disponible: " + room.isAvailable()));
    }
}