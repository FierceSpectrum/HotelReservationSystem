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
        roomService.registerRoom(room);
        System.out.println("Habitación registrada correctamente con ID: " + room.getId());
    }

    // Buscar habitacion por tipo
    public List<Room> findRoomByType(String type, Double price, Boolean available) {
        List<Room> rooms = roomService.searchRooms(type, price, available);
        System.out.println("Total de habitaciones encontradas: " + rooms.size());
        return rooms;
    }

    // Obtener todas las habitaciones
    public List<Room> getAllRooms() {
        List<Room> rooms = roomService.searchRooms(null, null, null);
        System.out.println("Total de habitaciones encontradas: " + rooms.size());
        return rooms;
    }
}
