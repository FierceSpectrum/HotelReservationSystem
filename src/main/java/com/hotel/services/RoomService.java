package com.hotel.services;

import com.hotel.models.Room;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomService {
    private List<Room> rooms = new ArrayList<>();

    // Registers a new room in the system
    public void registerRoom(Room room) {
        rooms.add(room);
    }

    // Searches for rooms based on type, price, and availability
    public List<Room> searchRooms(String type, Double price, Boolean available) {
        return rooms.stream()
                .filter(r -> (type == null || r.getType().equals(type)) &&
                        (price == null || r.getPrice() <= price) &&
                        (available == null || r.isAvailable() == available))
                .collect(Collectors.toList());
    }

    // Checks if a specific room is available
    public boolean checkAvailability(int roomId) {
        return rooms.stream()
                .anyMatch(r -> r.getId() == roomId && r.isAvailable());
    }

    // Updates the availability status of a room
    public void updateAvailability(int roomId, boolean available) {
        rooms.stream()
                .filter(r -> r.getId() == roomId)
                .findFirst()
                .ifPresent(r -> r.setAvailable(available));
    }
}
