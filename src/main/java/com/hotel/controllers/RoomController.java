package com.hotel.controllers;

import com.hotel.models.Room;
import com.hotel.services.RoomService;
import java.util.List;

public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Registrar una nueva habitación
    public void registerRoom(String type, double price, boolean available) {
        Room room = new Room(0, type, price, available);
        roomService.saveRoom(room);
        System.out.println("Habitación registrada correctamente");
    }

    // Buscar habitacion por tipo
    public List<Room> findRoomByType(String type) {
        return roomService.findRoomsByType(type);
    }

    // Obtener todas las habitaciones
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }
}
